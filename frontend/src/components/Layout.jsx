import { NavLink, useNavigate, Outlet } from 'react-router-dom'
import {
  LayoutDashboard,
  Package,
  Tags,
  Truck,
  ShoppingCart,
  Receipt,
  Warehouse,
  ClipboardList,
  Users,
  LogOut,
  Moon,
  Sun,
  Menu,
  X,
  Boxes,
} from 'lucide-react'
import { useState } from 'react'
import { useAuth } from '../context/AuthContext'
import { useTheme } from '../context/ThemeContext'

const navItems = [
  { to: '/dashboard', label: 'Dashboard', icon: LayoutDashboard, adminOnly: true },
  { to: '/products', label: 'Products', icon: Package },
  { to: '/categories', label: 'Categories', icon: Tags },
  { to: '/suppliers', label: 'Suppliers', icon: Truck },
  { to: '/purchases', label: 'Purchases', icon: ShoppingCart },
  { to: '/sales', label: 'Sales', icon: Receipt },
  { to: '/inventory', label: 'Inventory', icon: Warehouse },
  { to: '/inventory/logs', label: 'Inventory Logs', icon: ClipboardList, adminOnly: true },
  { to: '/users', label: 'Users', icon: Users, adminOnly: true },
]

export default function Layout() {
  const { user, logout, isAdmin } = useAuth()
  const { isDark, toggleTheme } = useTheme()
  const navigate = useNavigate()
  const [sidebarOpen, setSidebarOpen] = useState(false)

  const handleLogout = () => {
    logout()
    navigate('/login')
  }

  const filteredNav = navItems.filter((item) => !item.adminOnly || isAdmin)

  const linkClass = ({ isActive }) =>
    `flex items-center gap-3 rounded-xl px-3 py-2.5 text-sm font-medium transition-all ${
      isActive
        ? 'bg-primary-500/10 text-primary-600 dark:bg-primary-500/20 dark:text-primary-300'
        : 'text-surface-700 hover:bg-surface-100 dark:text-surface-200 dark:hover:bg-surface-800'
    }`

  const sidebar = (
    <aside className="flex h-full flex-col border-r border-surface-200 bg-white dark:border-surface-700 dark:bg-surface-900">
      <div className="flex items-center gap-3 border-b border-surface-200 px-5 py-5 dark:border-surface-700">
        <div className="flex h-9 w-9 items-center justify-center rounded-xl bg-gradient-to-br from-primary-500 to-primary-700 text-white shadow-lg">
          <Boxes size={18} />
        </div>
        <div>
          <p className="text-sm font-bold text-surface-900 dark:text-white">Inventory</p>
          <p className="text-xs text-surface-700 dark:text-surface-200">Management System</p>
        </div>
      </div>

      <nav className="flex-1 space-y-1 overflow-y-auto p-3">
        {filteredNav.map((item) => (
          <NavLink
            key={item.to}
            to={item.to}
            className={linkClass}
            onClick={() => setSidebarOpen(false)}
          >
            <item.icon size={18} />
            {item.label}
          </NavLink>
        ))}
      </nav>

      <div className="border-t border-surface-200 p-3 dark:border-surface-700">
        <div className="mb-2 rounded-xl bg-surface-50 px-3 py-2.5 dark:bg-surface-800">
          <p className="truncate text-sm font-medium text-surface-900 dark:text-white">{user?.name}</p>
          <p className="truncate text-xs text-surface-700 dark:text-surface-200">{user?.email}</p>
          <span className="mt-1 inline-block rounded-md bg-primary-500/10 px-2 py-0.5 text-xs font-medium text-primary-600 dark:text-primary-300">
            {user?.role}
          </span>
        </div>
        <button
          onClick={toggleTheme}
          className="flex w-full items-center gap-3 rounded-xl px-3 py-2.5 text-sm font-medium text-surface-700 hover:bg-surface-100 dark:text-surface-200 dark:hover:bg-surface-800"
        >
          {isDark ? <Sun size={18} /> : <Moon size={18} />}
          {isDark ? 'Light Mode' : 'Dark Mode'}
        </button>
        <button
          onClick={handleLogout}
          className="flex w-full items-center gap-3 rounded-xl px-3 py-2.5 text-sm font-medium text-rose-600 hover:bg-rose-50 dark:text-rose-400 dark:hover:bg-rose-500/10"
        >
          <LogOut size={18} />
          Sign Out
        </button>
      </div>
    </aside>
  )

  return (
    <div className="flex min-h-screen bg-surface-50 dark:bg-surface-950">
      <div className="hidden w-64 shrink-0 lg:block">{sidebar}</div>

      {sidebarOpen && (
        <div className="fixed inset-0 z-40 lg:hidden">
          <div className="absolute inset-0 bg-black/50" onClick={() => setSidebarOpen(false)} />
          <div className="relative h-full w-64">{sidebar}</div>
        </div>
      )}

      <div className="flex flex-1 flex-col">
        <header className="sticky top-0 z-30 flex items-center gap-4 border-b border-surface-200 bg-white/80 px-4 py-3 backdrop-blur-md dark:border-surface-700 dark:bg-surface-900/80 lg:hidden">
          <button
            onClick={() => setSidebarOpen(true)}
            className="rounded-lg p-2 text-surface-700 hover:bg-surface-100 dark:text-surface-200 dark:hover:bg-surface-800"
          >
            {sidebarOpen ? <X size={20} /> : <Menu size={20} />}
          </button>
          <div className="flex items-center gap-2">
            <Boxes size={20} className="text-primary-500" />
            <span className="font-bold text-surface-900 dark:text-white">Inventory</span>
          </div>
        </header>

        <main className="flex-1 p-4 sm:p-6 lg:p-8">
          <Outlet />
        </main>
      </div>
    </div>
  )
}
