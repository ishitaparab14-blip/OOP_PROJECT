/**
 * CampusFind Global Frontend Scripts
 * Manages navigation state, notifications, toast alerts, and UI helpers.
 */

// Toast Alert System
function showToast(message, type = 'info') {
  let container = document.getElementById('toast-container');
  if (!container) {
    container = document.createElement('div');
    container.id = 'toast-container';
    document.body.appendChild(container);
  }

  const toast = document.createElement('div');
  toast.className = `toast ${type}`;
  toast.innerHTML = `
    <div>${message}</div>
    <button style="background:none; border:none; color:inherit; cursor:pointer; font-size:1.1rem;" onclick="this.parentElement.remove()">✕</button>
  `;

  container.appendChild(toast);

  setTimeout(() => {
    toast.style.opacity = '0';
    toast.style.transform = 'translateX(100%)';
    toast.style.transition = 'all 0.3s ease';
    setTimeout(() => toast.remove(), 300);
  }, 4500);
}

// Format date helper
function formatDate(dateString) {
  if (!dateString) return '';
  const date = new Date(dateString);
  return date.toLocaleDateString('en-US', {
    month: 'short',
    day: 'numeric',
    year: 'numeric'
  });
}

function formatDateTime(dateTimeString) {
  if (!dateTimeString) return '';
  const date = new Date(dateTimeString);
  return date.toLocaleDateString('en-US', {
    month: 'short',
    day: 'numeric',
    hour: '2-digit',
    minute: '2-digit'
  });
}

// Category and Status badge builders
function getCategoryBadge(category) {
  const map = {
    'ELECTRONICS': { name: 'Electronics', color: 'badge-found' },
    'BOOKS_STATIONERY': { name: 'Books & Notes', color: 'badge-resolved' },
    'BAGS_WALLETS': { name: 'Bags & Wallets', color: 'badge-returned' },
    'KEYS_CARDS': { name: 'Keys & IDs', color: 'badge-pending' },
    'CLOTHING_ACCESSORIES': { name: 'Clothing', color: 'badge-archived' },
    'OTHER': { name: 'Miscellaneous', color: 'badge-archived' }
  };
  const c = map[category] || { name: category, color: 'badge-archived' };
  return `<span class="badge ${c.color}">${c.name}</span>`;
}

function getStatusBadge(status) {
  switch (status) {
    case 'OPEN':
      return `<span class="badge badge-open">● Active / Open</span>`;
    case 'CLAIM_PENDING':
      return `<span class="badge badge-pending">⏳ Claim Under Review</span>`;
    case 'RETURNED':
      return `<span class="badge badge-returned">✓ Returned to Owner</span>`;
    case 'RESOLVED':
      return `<span class="badge badge-resolved">✓ Resolved</span>`;
    case 'ARCHIVED':
      return `<span class="badge badge-archived">Archived</span>`;
    default:
      return `<span class="badge">${status}</span>`;
  }
}

function getTypeBadge(type) {
  if (type === 'LOST') {
    return `<span class="badge badge-lost">🔍 LOST ITEM</span>`;
  } else {
    return `<span class="badge badge-found">📍 FOUND ITEM</span>`;
  }
}

// Update Global Navigation
async function initNavbar() {
  const authNav = document.getElementById('nav-auth-container');
  if (!authNav) return;

  const user = await API.getCurrentUser();

  if (user) {
    // Logged in
    let unreadCount = 0;
    try {
      const res = await API.getUnreadNotificationCount();
      unreadCount = res?.unreadCount || 0;
    } catch (e) {
      // ignore
    }

    const isAdmin = user.role === 'ROLE_ADMIN';
    const roleBadge = isAdmin 
      ? `<span class="badge" style="background:#fef3c7; color:#b45309; border:1px solid #fde68a;">Admin</span>`
      : `<span class="badge" style="background:#e0e7ff; color:#4338ca;">Student</span>`;

    authNav.innerHTML = `
      <div style="display:flex; align-items:center; gap: 1rem;">
        <a href="/report.html" class="btn btn-primary btn-sm">
          <span>+</span> Report Item
        </a>
        <a href="/dashboard.html" class="notification-bell" title="My Notifications">
          🔔
          ${unreadCount > 0 ? `<span class="notification-count">${unreadCount}</span>` : ''}
        </a>
        <div style="display:flex; align-items:center; gap: 0.5rem;">
          <a href="/dashboard.html" style="font-weight:600; font-size:0.9rem; color:var(--text-primary);">
            ${user.fullName}
          </a>
          ${roleBadge}
        </div>
        ${isAdmin ? `<a href="/admin.html" class="btn btn-amber btn-sm">Admin Portal</a>` : ''}
        <button id="logout-btn" class="btn btn-outline btn-sm">Logout</button>
      </div>
    `;

    document.getElementById('logout-btn')?.addEventListener('click', async () => {
      try {
        await API.logout();
        showToast('Logged out successfully', 'info');
        setTimeout(() => window.location.href = '/index.html', 400);
      } catch (err) {
        window.location.href = '/index.html';
      }
    });

  } else {
    // Guest
    authNav.innerHTML = `
      <div style="display:flex; align-items:center; gap: 0.75rem;">
        <a href="/login.html" class="btn btn-outline btn-sm">Login</a>
        <a href="/register.html" class="btn btn-primary btn-sm">Register</a>
      </div>
    `;
  }
}

document.addEventListener('DOMContentLoaded', () => {
  initNavbar();
});
