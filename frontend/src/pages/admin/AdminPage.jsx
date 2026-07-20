import { useEffect, useState } from "react";
import { Users, FolderKanban, CheckSquare, Shield } from "lucide-react";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import api from "@/api/axios";
import ENDPOINTS from "@/api/endpoints";

export default function AdminPage() {
  const [analytics, setAnalytics] = useState(null);
  const [users, setUsers] = useState([]);
  const [auditLogs, setAuditLogs] = useState([]);

  useEffect(() => {
    api.get(ENDPOINTS.ADMIN.ANALYTICS).then((res) => setAnalytics(res.data.data)).catch(() => {});
    api.get(ENDPOINTS.ADMIN.USERS).then((res) => setUsers(res.data.data?.content || [])).catch(() => {});
    api.get(ENDPOINTS.ADMIN.AUDIT_LOGS).then((res) => setAuditLogs(res.data.data?.content || [])).catch(() => {});
  }, []);

  const stats = [
    { label: "Total Users", value: analytics?.totalUsers ?? 0, icon: Users, color: "text-blue-500" },
    { label: "Total Projects", value: analytics?.totalProjects ?? 0, icon: FolderKanban, color: "text-emerald-500" },
    { label: "Total Tasks", value: analytics?.totalTasks ?? 0, icon: CheckSquare, color: "text-amber-500" },
    { label: "Active Users", value: analytics?.activeUsers ?? 0, icon: Shield, color: "text-violet-500" },
  ];

  return (
    <div>
      <div className="mb-8">
        <h1 className="text-3xl font-bold">Admin Panel</h1>
        <p className="text-slate-500">System management and analytics</p>
      </div>

      <div className="grid grid-cols-1 md:grid-cols-4 gap-6 mb-8">
        {stats.map(({ label, value, icon: Icon, color }) => (
          <Card key={label} className="border-0 shadow-sm">
            <CardContent className="p-6 flex items-center gap-4">
              <Icon className={color} size={24} />
              <div>
                <p className="text-sm text-slate-500">{label}</p>
                <p className="text-2xl font-bold">{value}</p>
              </div>
            </CardContent>
          </Card>
        ))}
      </div>

      <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
        <Card className="border-0 shadow-sm">
          <CardHeader><CardTitle>Users</CardTitle></CardHeader>
          <CardContent>
            <div className="space-y-2">
              {users.map((u) => (
                <div key={u.id} className="flex items-center justify-between p-3 bg-slate-50 rounded-lg">
                  <div>
                    <p className="text-sm font-medium">{u.firstName} {u.lastName}</p>
                    <p className="text-xs text-slate-400">{u.email}</p>
                  </div>
                  <span className="text-xs bg-slate-200 px-2 py-0.5 rounded">{u.roles?.[0] || "USER"}</span>
                </div>
              ))}
            </div>
          </CardContent>
        </Card>

        <Card className="border-0 shadow-sm">
          <CardHeader><CardTitle>Audit Logs</CardTitle></CardHeader>
          <CardContent>
            <div className="space-y-2">
              {auditLogs.map((log) => (
                <div key={log.id} className="p-3 bg-slate-50 rounded-lg">
                  <p className="text-sm font-medium">{log.action} · {log.entityType}</p>
                  <p className="text-xs text-slate-400">{log.details}</p>
                </div>
              ))}
              {auditLogs.length === 0 && <p className="text-sm text-slate-400">No audit logs yet</p>}
            </div>
          </CardContent>
        </Card>
      </div>
    </div>
  );
}
