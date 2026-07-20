import { useEffect, useState } from "react";
import { Container, Play, Square, RotateCcw, Trash2, FileText } from "lucide-react";
import toast from "react-hot-toast";
import { Button } from "@/components/ui/button";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import api from "@/api/axios";
import ENDPOINTS from "@/api/endpoints";

export default function DockerPage() {
  const [containers, setContainers] = useState([]);
  const [images, setImages] = useState([]);
  const [logs, setLogs] = useState(null);

  useEffect(() => {
    api.get(ENDPOINTS.DOCKER.CONTAINERS).then((res) => setContainers(res.data.data || [])).catch(() => {});
    api.get(ENDPOINTS.DOCKER.IMAGES).then((res) => setImages(res.data.data || [])).catch(() => {});
  }, []);

  const viewLogs = async (id) => {
    const res = await api.get(ENDPOINTS.DOCKER.LOGS(id));
    setLogs(res.data.data);
  };

  const handleAction = async (action, id) => {
    try {
      await api.post(`/api/docker/containers/${id}/${action}`);
      toast.success(`Container ${action}ed`);
    } catch {
      toast.error("Action failed");
    }
  };

  return (
    <div>
      <div className="mb-8">
        <h1 className="text-3xl font-bold">Docker Dashboard</h1>
        <p className="text-black">Manage containers, images, and volumes</p>
      </div>

      <div className="grid grid-cols-1 lg:grid-cols-2 gap-6 mb-6">
        <Card className="border-0 shadow-sm">
          <CardHeader><CardTitle className="flex items-center gap-2"><Container size={18} /> Containers</CardTitle></CardHeader>
          <CardContent className="space-y-3">
            {containers.map((c) => (
              <div key={c.id} className="flex items-center justify-between p-4 bg-slate-50 rounded-lg">
                <div>
                  <p className="font-medium text-sm">{c.name}</p>
                  <p className="text-xs text-slate-400">{c.image} · {c.status}</p>
                </div>
                <div className="flex gap-1">
                  <Button size="sm" variant="outline" onClick={() => viewLogs(c.id)}><FileText size={14} /></Button>
                  <Button size="sm" variant="outline" onClick={() => handleAction("restart", c.id)}><RotateCcw size={14} /></Button>
                  <Button size="sm" variant="outline" onClick={() => handleAction("stop", c.id)}><Square size={14} /></Button>
                </div>
              </div>
            ))}
          </CardContent>
        </Card>

        <Card className="border-0 shadow-sm">
          <CardHeader><CardTitle>Images</CardTitle></CardHeader>
          <CardContent className="space-y-3">
            {images.map((img) => (
              <div key={img.id} className="flex items-center justify-between p-4 bg-slate-50 rounded-lg">
                <div>
                  <p className="font-medium text-sm">{img.name}:{img.tag}</p>
                  <p className="text-xs text-slate-400">{img.size}</p>
                </div>
              </div>
            ))}
          </CardContent>
        </Card>
      </div>

      {logs && (
        <Card className="border-0 shadow-sm">
          <CardHeader><CardTitle>Container Logs</CardTitle></CardHeader>
          <CardContent>
            <pre className="bg-slate-950 text-green-400 p-4 rounded-lg text-sm font-mono overflow-auto max-h-64 whitespace-pre-wrap">{logs}</pre>
          </CardContent>
        </Card>
      )}
    </div>
  );
}
