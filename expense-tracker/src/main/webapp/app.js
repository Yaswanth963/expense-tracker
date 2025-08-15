const API_BASE =
    window.location.port === "8080"
        ? "/expense-tracker"
        : "http://localhost:8080/expense-tracker";

function showErrorDialog(msg) {
    document.getElementById('error-message').textContent = msg;
    document.getElementById('error-dialog').classList.remove('hidden');
}

function hideErrorDialog() {
    document.getElementById('error-dialog').classList.add('hidden');
}

function showAppSection(show) {
    document.getElementById('auth-section').classList.toggle('hidden', show);
    document.getElementById('app-section').classList.toggle('hidden', !show);
    const logoutBtn = document.getElementById('logout-btn');
    if (show) {
        logoutBtn.classList.remove('hidden');
    } else {
        logoutBtn.classList.add('hidden');
    }
}

function setBalance(balance) {
    const balanceElem = document.getElementById('balance');
    balanceElem.textContent = balance.toFixed(2);
    balanceElem.classList.remove('balance-positive', 'balance-negative');
    if (balance > 0) {
        balanceElem.classList.add('balance-positive');
    } else if (balance < 0) {
        balanceElem.classList.add('balance-negative');
    }
}

function renderExpenses(expenses) {
    const tbody = document.getElementById('expenses-tbody');
    tbody.innerHTML = '';
    let balance = 0;
    expenses.forEach(exp => {
        const tr = document.createElement('tr');
        tr.innerHTML = `
            <td>${exp.name || ''}</td>
            <td>${exp.date}</td>
            <td class="${exp.type}">${exp.type.charAt(0).toUpperCase() + exp.type.slice(1)}</td>
            <td>${exp.amount.toFixed(2)}</td>
            <td>
                <button class="delete-btn" data-id="${exp.id}" data-userid="${exp.userId}" title="Delete">🗑️</button>
            </td>`;
        tbody.appendChild(tr);
        if (exp.type === 'credit') balance += exp.amount;
        else if (exp.type === 'debit') balance -= exp.amount;
    });
    setBalance(balance);
    addDeleteListeners();
}

function addDeleteListeners() {
    document.querySelectorAll('.delete-btn').forEach(btn => {
        btn.onclick = function() {
            const id = this.getAttribute('data-id');
            const userId = this.getAttribute('data-userid');
            if (confirm('Are you sure you want to delete this expense?')) {
                fetch(`${API_BASE}/expenses?id=${id}&userId=${userId}`, {
                    method: 'DELETE'
                })
                .then(res => res.json())
                .then(data => {
                    if (data.success) {
                        fetchExpenses();
                    } else {
                        showErrorDialog('Failed to delete expense.');
                    }
                })
                .catch(() => showErrorDialog('Network or server error while deleting expense.'));
            }
        };
    });
}

function fetchExpenses() {
    const userId = localStorage.getItem('userId');
    fetch(`${API_BASE}/expenses?userId=${userId}`)
        .then(res => res.json())
        .then(data => {
            const expenses = Array.isArray(data) ? data.filter(e => e.userId == userId) : [];
            renderExpenses(expenses);
        })
        .catch(() => showErrorDialog('Failed to fetch expenses.'));
}

document.getElementById('register-form').onsubmit = function (e) {
    e.preventDefault();
    const email = document.getElementById('register-email').value;
    const password = document.getElementById('register-password').value;
    fetch(`${API_BASE}/register`, {
        method: 'POST',
        headers: {'Content-Type': 'application/x-www-form-urlencoded'},
        body: `email=${encodeURIComponent(email)}&password=${encodeURIComponent(password)}`
    })
        .then(res => res.json())
        .then(data => {
            if (data.success) {
                showErrorDialog('Registration successful! Please login.');
            } else {
                showErrorDialog('Registration failed.');
            }
        })
        .catch(() => showErrorDialog('Network or server error during registration.'));
};

document.getElementById('login-form').onsubmit = function (e) {
    e.preventDefault();
    const email = document.getElementById('login-email').value;
    const password = document.getElementById('login-password').value;
    fetch(`${API_BASE}/login`, {
        method: 'POST',
        headers: {'Content-Type': 'application/x-www-form-urlencoded'},
        body: `email=${encodeURIComponent(email)}&password=${encodeURIComponent(password)}`
    })
        .then(res => res.json())
        .then(data => {
            if (data.success && data.userId) {
                localStorage.setItem('userId', data.userId);
                showAppSection(true);
                fetchExpenses();
            } else {
                showErrorDialog('Login failed.');
            }
        })
        .catch(() => showErrorDialog('Network or server error during login.'));
};

document.getElementById('expense-form').onsubmit = function (e) {
    e.preventDefault();
    const userId = localStorage.getItem('userId');
    const amount = document.getElementById('amount').value;
    const type = document.getElementById('type').value;
    const date = document.getElementById('date').value;
    const name = document.getElementById('name').value;
    fetch(`${API_BASE}/expenses`, {
        method: 'POST',
        headers: {'Content-Type': 'application/x-www-form-urlencoded'},
        body: `userId=${userId}&amount=${amount}&type=${type}&date=${date}&name=${encodeURIComponent(name)}`
    })
        .then(res => res.json())
        .then(data => {
            if (data.success) {
                fetchExpenses();
                document.getElementById('expense-form').reset();
            } else {
                showErrorDialog('Failed to add expense.');
            }
        })
        .catch(() => showErrorDialog('Network or server error while adding expense.'));
};

document.getElementById('logout-btn').onclick = function () {
    localStorage.removeItem('userId');
    showAppSection(false);
};

window.onload = function () {
    if (localStorage.getItem('userId')) {
        showAppSection(true);
        fetchExpenses();
    }
};
