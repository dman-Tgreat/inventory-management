const API_BASE = import.meta.env.VITE_API_URL || '/api'

function getStoredUser() {
  try {
    return JSON.parse(localStorage.getItem('user') || 'null')
  } catch {
    return null
  }
}

export async function apiRequest(path, options = {}) {
  const user = getStoredUser()
  const headers = {
    'Content-Type': 'application/json',
    ...options.headers,
  }
  if (user?.role) {
    headers['X-Role'] = user.role
  }

  const response = await fetch(`${API_BASE}${path}`, {
    ...options,
    headers,
  })

  if (!response.ok) {
    const text = await response.text()
    throw new Error(text || response.statusText)
  }

  if (response.status === 204) return null
  return response.json()
}

export const authApi = {
  login: (data) => apiRequest('/auth/login', { method: 'POST', body: JSON.stringify(data) }),
  register: (data) => apiRequest('/auth/register', { method: 'POST', body: JSON.stringify(data) }),
}

export const productsApi = {
  getAll: () => apiRequest('/products'),
  getById: (id) => apiRequest(`/products/${id}`),
  create: (data) => apiRequest('/products', { method: 'POST', body: JSON.stringify(data) }),
  update: (id, data) => apiRequest(`/products/${id}`, { method: 'PUT', body: JSON.stringify(data) }),
  delete: (id) => apiRequest(`/products/${id}`, { method: 'DELETE' }),
  search: (keyword) => apiRequest(`/products/search?keyword=${encodeURIComponent(keyword)}`),
  filter: (params) => {
    const query = new URLSearchParams()
    if (params.categoryId) query.set('categoryId', params.categoryId)
    if (params.supplierId) query.set('supplierId', params.supplierId)
    if (params.lowStock) query.set('lowStock', 'true')
    return apiRequest(`/products/filter?${query}`)
  },
}

export const categoriesApi = {
  getAll: () => apiRequest('/categories'),
  create: (data) => apiRequest('/categories', { method: 'POST', body: JSON.stringify(data) }),
}

export const suppliersApi = {
  getAll: () => apiRequest('/suppliers'),
  create: (data) => apiRequest('/suppliers', { method: 'POST', body: JSON.stringify(data) }),
}

export const purchasesApi = {
  getAll: () => apiRequest('/purchases'),
  create: (data) => apiRequest('/purchases', { method: 'POST', body: JSON.stringify(data) }),
}

export const salesApi = {
  getAll: () => apiRequest('/sales'),
  create: (data) => apiRequest('/sales', { method: 'POST', body: JSON.stringify(data) }),
}

export const inventoryApi = {
  getSummary: () => apiRequest('/inventory'),
  getLowStock: () => apiRequest('/inventory/low-stock'),
  getOutOfStock: () => apiRequest('/inventory/out-of-stock'),
  getLogs: () => apiRequest('/inventory/logs'),
}

export const dashboardApi = {
  get: () => apiRequest('/dashboard'),
}

export const usersApi = {
  getAll: () => apiRequest('/users'),
  delete: (id) => apiRequest(`/users/${id}`, { method: 'DELETE' }),
}
