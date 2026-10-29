/**
 * CampusFind REST API Client Wrapper
 * Handles session cookies, status checks, and uniform error reporting.
 */
const API = {
  baseUrl: '',

  async request(endpoint, options = {}) {
    const config = {
      headers: {
        'Content-Type': 'application/json',
        'Accept': 'application/json',
        ...options.headers,
      },
      credentials: 'same-origin', // Sends and receives session cookies automatically
      ...options,
    };

    if (config.body && typeof config.body === 'object') {
      config.body = JSON.stringify(config.body);
    }

    try {
      const response = await fetch(`${this.baseUrl}${endpoint}`, config);

      if (response.status === 204) {
        return null;
      }

      // Check if response is JSON
      const contentType = response.headers.get('content-type');
      let data = null;
      if (contentType && contentType.includes('application/json')) {
        data = await response.json();
      } else {
        const text = await response.text();
        data = { message: text };
      }

      if (!response.ok) {
        const errorMsg = data?.message || data?.error || `Request failed with status ${response.status}`;
        const error = new Error(errorMsg);
        error.status = response.status;
        error.data = data;
        throw error;
      }

      return data;
    } catch (err) {
      console.error(`API Error on [${options.method || 'GET'} ${endpoint}]:`, err);
      throw err;
    }
  },

  get(endpoint) {
    return this.request(endpoint, { method: 'GET' });
  },

  post(endpoint, body) {
    return this.request(endpoint, { method: 'POST', body });
  },

  put(endpoint, body) {
    return this.request(endpoint, { method: 'PUT', body });
  },

  patch(endpoint, body) {
    return this.request(endpoint, { method: 'PATCH', body });
  },

  delete(endpoint) {
    return this.request(endpoint, { method: 'DELETE' });
  },

  // Auth endpoints
  async getCurrentUser() {
    try {
      return await this.get('/api/auth/me');
    } catch (err) {
      return null;
    }
  },

  login(credentials) {
    return this.post('/api/auth/login', credentials);
  },

  register(userData) {
    return this.post('/api/auth/register', userData);
  },

  logout() {
    return this.post('/api/auth/logout', {});
  },

  // Items endpoints
  getItems(params = {}) {
    const query = new URLSearchParams();
    if (params.type) query.append('type', params.type);
    if (params.category) query.append('category', params.category);
    if (params.status) query.append('status', params.status);
    if (params.location) query.append('location', params.location);
    if (params.query) query.append('query', params.query);
    const queryString = query.toString() ? `?${query.toString()}` : '';
    return this.get(`/api/items${queryString}`);
  },

  getItem(id) {
    return this.get(`/api/items/${id}`);
  },

  getItemMatches(id) {
    return this.get(`/api/items/${id}/matches`);
  },

  getMyItems() {
    return this.get('/api/items/my');
  },

  createItem(itemData) {
    return this.post('/api/items', itemData);
  },

  updateItem(id, itemData) {
    return this.put(`/api/items/${id}`, itemData);
  },

  updateItemStatus(id, status) {
    return this.patch(`/api/items/${id}/status`, { status });
  },

  // Claims endpoints
  submitClaim(itemId, claimData) {
    return this.post(`/api/claims/items/${itemId}`, claimData);
  },

  getMyClaims() {
    return this.get('/api/claims/my');
  },

  getClaimsForItem(itemId) {
    return this.get(`/api/claims/items/${itemId}`);
  },

  withdrawClaim(claimId) {
    return this.post(`/api/claims/${claimId}/withdraw`, {});
  },

  // Admin endpoints
  getAdminClaims() {
    return this.get('/api/admin/claims');
  },

  submitAdminDecision(claimId, decisionData) {
    return this.post(`/api/admin/claims/${claimId}/decision`, decisionData);
  },

  getAdminItems() {
    return this.get('/api/admin/items');
  },

  adminChangeItemStatus(id, status) {
    return this.patch(`/api/admin/items/${id}/status`, { status });
  },

  // Notifications endpoints
  getNotifications() {
    return this.get('/api/notifications');
  },

  getUnreadNotificationCount() {
    return this.get('/api/notifications/unread-count');
  },

  markNotificationRead(id) {
    return this.patch(`/api/notifications/${id}/read`, {});
  },

  markAllNotificationsRead() {
    return this.post('/api/notifications/mark-all-read', {});
  },

  // Statistics
  getStats() {
    return this.get('/api/stats');
  }
};
