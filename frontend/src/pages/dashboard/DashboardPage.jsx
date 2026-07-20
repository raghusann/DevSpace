import { useEffect, useState } from "react";
import { FolderKanban, CheckSquare, Bell, Activity } from "lucide-react";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import api from "@/api/axios";
import ENDPOINTS from "@/api/endpoints";

export default function DashboardPage() {
  const [stats, setStats] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    api.get(ENDPOINTS.DASHBOARD.STATS)
      .then((res) => setStats(res.data.data))
      .catch(() => {})
      .finally(() => setLoading(false));
  }, []);

  const cards = [
    {
      title: "Projects",
      value: stats?.projectCount ?? 0,
      icon: FolderKanban,
      color: "from-blue-500 to-cyan-500",
    },
    {
      title: "Tasks",
      value: stats?.taskCount ?? 0,
      icon: CheckSquare,
      color: "from-emerald-500 to-green-500",
    },
    {
      title: "Notifications",
      value: stats?.unreadNotifications ?? 0,
      icon: Bell,
      color: "from-amber-500 to-orange-500",
    },
    {
      title: "Activities",
      value: stats?.recentActivities?.length ?? 0,
      icon: Activity,
      color: "from-violet-500 to-fuchsia-500",
    },
  ];

  return (
    <div className="space-y-8">
      {/* Header */}
      <div>
        <h1 className="text-4xl font-bold tracking-tight text-slate-900">
          Dashboard
        </h1>
        <p className="mt-2 text-slate-600 text-lg">
          Overview of your developer workspace
        </p>
      </div>

      {/* Stats */}
      <div className="grid grid-cols-1 gap-6 md:grid-cols-2 lg:grid-cols-4">
        {cards.map(({ title, value, icon: Icon, color }) => (
          <Card
            key={title}
            className="group border border-slate-200/70 bg-white/80 backdrop-blur-md shadow-sm hover:shadow-xl hover:-translate-y-1 transition-all duration-300 rounded-2xl"
          >
            <CardContent className="p-6">
              <div className="flex items-center justify-between">
                <div>
                  <p className="text-sm font-medium text-slate-500">
                    {title}
                  </p>

                  <p className="mt-2 text-4xl font-bold text-slate-900">
                    {loading ? "..." : value}
                  </p>
                </div>

                <div
                  className={`flex h-14 w-14 items-center justify-center rounded-2xl bg-gradient-to-br ${color} shadow-lg`}
                >
                  <Icon className="text-white" size={24} />
                </div>
              </div>
            </CardContent>
          </Card>
        ))}
      </div>

      {/* Bottom */}
      <div className="grid grid-cols-1 gap-6 lg:grid-cols-2">
        {/* Activities */}
        <Card className="border border-slate-200/70 bg-white/80 backdrop-blur-md shadow-sm rounded-2xl">
          <CardHeader>
            <CardTitle className="text-slate-900">
              Recent Activities
            </CardTitle>
          </CardHeader>

          <CardContent>
            {stats?.recentActivities?.length > 0 ? (
              <ul className="space-y-4">
                {stats.recentActivities.map((a) => (
                  <li
                    key={a.id}
                    className="flex items-center gap-3 rounded-xl p-3 hover:bg-slate-50 transition"
                  >
                    <div className="h-2.5 w-2.5 rounded-full bg-blue-500" />

                    <span className="text-sm text-slate-700">
                      {a.details || a.action}
                    </span>
                  </li>
                ))}
              </ul>
            ) : (
              <p className="text-sm text-slate-500">
                No recent activities
              </p>
            )}
          </CardContent>
        </Card>

        {/* Quick Actions */}
        <Card className="border border-slate-200/70 bg-white/80 backdrop-blur-md shadow-sm rounded-2xl">
          <CardHeader>
            <CardTitle className="text-slate-900">
              Quick Actions
            </CardTitle>
          </CardHeader>

          <CardContent className="grid grid-cols-2 gap-4">
            {[
              "Create Project",
              "API Tester",
              "Kanban Board",
              "View Docs",
            ].map((action) => (
              <button
                key={action}
                className="rounded-xl border border-slate-200 bg-slate-50 p-4 text-left text-sm font-medium text-slate-700 transition-all duration-300 hover:-translate-y-1 hover:border-blue-300 hover:bg-blue-50 hover:text-blue-700 hover:shadow-md"
              >
                {action}
              </button>
            ))}
          </CardContent>
        </Card>
      </div>
    </div>
  );
}