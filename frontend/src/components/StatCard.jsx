export default function StatCard({ title, value, icon: Icon, color = 'primary', subtitle }) {
  const colors = {
    primary: 'from-primary-500 to-primary-600',
    emerald: 'from-emerald-500 to-emerald-600',
    amber: 'from-amber-500 to-amber-600',
    rose: 'from-rose-500 to-rose-600',
    violet: 'from-violet-500 to-violet-600',
    cyan: 'from-cyan-500 to-cyan-600',
  }

  return (
    <div className="group relative overflow-hidden rounded-2xl border border-surface-200 bg-white p-5 shadow-sm transition-all hover:shadow-md dark:border-surface-700 dark:bg-surface-800">
      <div className="flex items-start justify-between">
        <div>
          <p className="text-sm font-medium text-surface-700 dark:text-surface-200">{title}</p>
          <p className="mt-2 text-2xl font-bold text-surface-900 dark:text-white">{value}</p>
          {subtitle && (
            <p className="mt-1 text-xs text-surface-700 dark:text-surface-200">{subtitle}</p>
          )}
        </div>
        {Icon && (
          <div
            className={`rounded-xl bg-gradient-to-br ${colors[color]} p-2.5 text-white shadow-lg`}
          >
            <Icon size={20} />
          </div>
        )}
      </div>
      <div
        className={`absolute -bottom-8 -right-8 h-24 w-24 rounded-full bg-gradient-to-br ${colors[color]} opacity-5 transition-transform group-hover:scale-110`}
      />
    </div>
  )
}
