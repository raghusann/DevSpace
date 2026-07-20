import { NavLink, useNavigate } from "react-router-dom";
import {
  LayoutDashboard,
  FolderKanban,
  FlaskConical,
  Code2,
  Container,
  Rocket,
  MessageSquare,
  Database,
  FileText,
  Shield,
  LogOut,
  User,
  Sparkles,
} from "lucide-react";

import useAuthStore from "@/store/authStore";
import authService from "@/services/auth/authService";

const navItems = [
  { to: "/dashboard", icon: LayoutDashboard, label: "Dashboard" },
  { to: "/projects", icon: FolderKanban, label: "Projects" },
  { to: "/api-tester", icon: FlaskConical, label: "API Tester" },
  { to: "/github", icon: Code2, label: "GitHub" },
  { to: "/docker", icon: Container, label: "Docker" },
  { to: "/deployments", icon: Rocket, label: "Deployments" },
  { to: "/chat", icon: MessageSquare, label: "Team Chat" },
  { to: "/database", icon: Database, label: "Database" },
  { to: "/docs", icon: FileText, label: "Documentation" },
  { to: "/profile", icon: User, label: "Profile" },
  { to: "/admin", icon: Shield, label: "Admin" },
];

export default function Sidebar() {
  const navigate = useNavigate();

  const { user, logout, refreshToken } = useAuthStore();

  const isAdmin = user?.roles?.includes("ADMIN");

  const initials = `${user?.firstName?.[0] || ""}${user?.lastName?.[0] || ""}`;

  const handleLogout = async () => {
    try {
      if (refreshToken) {
        await authService.logout(refreshToken);
      }
    } finally {
      logout();
      navigate("/login");
    }
  };

  return (
    <aside className="fixed left-0 top-0 z-50 flex h-screen w-64 flex-col border-r border-slate-800 bg-slate-950 text-white shadow-2xl">

      {/* Logo */}
      <div className="border-b border-slate-800 p-6">
        <div className="flex items-center gap-4">

          <div className="flex h-12 w-12 items-center justify-center rounded-2xl bg-gradient-to-br from-blue-500 via-indigo-500 to-violet-600 shadow-lg">
            <Sparkles size={22} className="text-white" />
          </div>

          <div>
            <h1 className="text-lg font-bold tracking-wide">
              DevSpace
            </h1>

            <p className="text-xs text-slate-400">
              Developer Workspace
            </p>
          </div>

        </div>
      </div>

      {/* Navigation */}

      <nav className="flex-1 overflow-y-auto px-4 py-5">

        <div className="space-y-2">

          {navItems
            .filter((item) => item.to !== "/admin" || isAdmin)
            .map(({ to, icon: Icon, label }) => (
              <NavLink
                key={to}
                to={to}
                className={({ isActive }) =>
                  `group relative flex items-center gap-3 rounded-xl px-4 py-3 text-sm font-medium transition-all duration-300 ${
                    isActive
                      ? "bg-gradient-to-r from-blue-600 to-indigo-600 text-white shadow-lg"
                      : "text-slate-400 hover:bg-slate-900 hover:text-white"
                  }`
                }
              >
                {({ isActive }) => (
                  <>
                    {isActive && (
                      <span className="absolute left-0 top-2 bottom-2 w-1 rounded-r-full bg-white" />
                    )}

                    <Icon
                      size={19}
                      className="transition-transform duration-300 group-hover:scale-110"
                    />

                    <span>{label}</span>
                  </>
                )}
              </NavLink>
            ))}
        </div>

      </nav>

      {/* User Section */}

      <div className="border-t border-slate-800 p-4">

        <div className="rounded-2xl border border-slate-800 bg-slate-900/80 p-4">

          <div className="flex items-center gap-3">

            <div className="relative">

              <div className="flex h-12 w-12 items-center justify-center rounded-full bg-gradient-to-br from-blue-500 to-violet-600 text-sm font-bold text-white shadow-md">
                {initials}
              </div>

              <span className="absolute bottom-0 right-0 h-3.5 w-3.5 rounded-full border-2 border-slate-900 bg-emerald-500" />

            </div>

            <div className="min-w-0 flex-1">

              <h3 className="truncate text-sm font-semibold text-white">
                {user?.firstName} {user?.lastName}
              </h3>

              <p className="truncate text-xs text-slate-400">
                {user?.email}
              </p>

              <span className="mt-2 inline-flex rounded-full bg-blue-600/20 px-2.5 py-1 text-[10px] font-semibold uppercase tracking-wider text-blue-300">
                {user?.roles?.[0] || "USER"}
              </span>

            </div>

          </div>

          <button
            onClick={handleLogout}
            className="mt-5 flex w-full items-center justify-center gap-2 rounded-xl border border-red-500/20 bg-red-500/10 px-4 py-3 text-sm font-medium text-red-400 transition-all duration-300 hover:bg-red-500 hover:text-white"
          >
            <LogOut size={17} />
            Sign Out
          </button>

        </div>

      </div>

    </aside>
  );
}