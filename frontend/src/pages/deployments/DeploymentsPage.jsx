import { useEffect, useState } from "react";
import { Rocket, RotateCcw } from "lucide-react";
import toast from "react-hot-toast";
import { Button } from "@/components/ui/button";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Input } from "@/components/ui/input";
import api from "@/api/axios";

export default function DeploymentsPage() {
  const [deployments, setDeployments] = useState([]);
  const [projectId, setProjectId] = useState("1");
  const [form, setForm] = useState({
    environment: "production",
    version: "v1.0.0",
  });
  useEffect(() => {
    fetchDeployments();
  }, [projectId]);

  const fetchDeployments = () => {
    if (!projectId) return;
    api
      .get(`/api/projects/${projectId}/deployments`)
      .then((res) => setDeployments(res.data.data?.content || []))
      .catch(() => {});
  };

  const handleDeploy = async () => {
    try {
      await api.post(`/api/projects/${projectId}/deployments`, form);
      toast.success("Deployment started!");
      fetchDeployments();
    } catch {
      toast.error("Deployment failed");
    }
  };

  const handleRollback = async (id) => {
    try {
      await api.post(`/api/projects/${projectId}/deployments/${id}/rollback`);
      toast.success("Rollback initiated");
      fetchDeployments();
    } catch {
      toast.error("Rollback failed");
    }
  };

  return (
    <div>
      <div className="mb-8">
        <h1 className="text-3xl font-bold">Deployments</h1>
        <p className="text-black">Monitor and manage deployments</p>
      </div>

      <Card className="border-0 shadow-sm mb-6">
        <CardContent className="p-6">
          <div className="flex gap-4 items-end">
            <div>
              <label className="text-sm text-slate-500">Project ID</label>
              <Input
                value={projectId}
                onChange={(e) => setProjectId(e.target.value)}
                className="w-32 mt-1"
              />
            </div>
            <div>
              <label className="text-sm text-slate-500">Environment</label>
              <Input
                value={form.environment}
                onChange={(e) =>
                  setForm({ ...form, environment: e.target.value })
                }
                className="w-40 mt-1"
              />
            </div>
            <div>
              <label className="text-sm text-slate-500">Version</label>
              <Input
                value={form.version}
                onChange={(e) => setForm({ ...form, version: e.target.value })}
                className="w-40 mt-1"
              />
            </div>
            <Button onClick={handleDeploy}>
              <Rocket size={16} className="mr-2" /> Deploy
            </Button>
            <Button variant="outline" onClick={fetchDeployments}>
              Refresh
            </Button>
          </div>
        </CardContent>
      </Card>

      <div className="space-y-4">
        {deployments.map((d) => (
          <Card key={d.id} className="border-0 shadow-sm">
            <CardContent className="p-4 flex items-center justify-between">
              <div>
                <p className="font-medium">
                  {d.environment} · {d.version}
                </p>
                <p className="text-sm text-slate-400">
                  {new Date(d.createdAt).toLocaleString()}
                </p>
              </div>
              <div className="flex items-center gap-3">
                <span
                  className={`text-xs px-2 py-1 rounded-full font-medium ${
                    d.status === "SUCCESS"
                      ? "bg-emerald-100 text-emerald-700"
                      : d.status === "FAILED"
                        ? "bg-red-100 text-red-700"
                        : "bg-amber-100 text-amber-700"
                  }`}
                >
                  {d.status}
                </span>
                <Button
                  size="sm"
                  variant="outline"
                  onClick={() => handleRollback(d.id)}
                >
                  <RotateCcw size={14} className="mr-1" /> Rollback
                </Button>
              </div>
            </CardContent>
          </Card>
        ))}
      </div>
    </div>
  );
}
