import { useEffect, useState } from "react";
import { Code2, GitBranch, GitPullRequest, Link } from "lucide-react";
import toast from "react-hot-toast";
import { Button } from "@/components/ui/button";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Input } from "@/components/ui/input";
import api from "@/api/axios";
import ENDPOINTS from "@/api/endpoints";

export default function GitHubPage() {
  const [status, setStatus] = useState(null);
  const [username, setUsername] = useState("");
  const [token, setToken] = useState("");

  useEffect(() => {
    api.get(ENDPOINTS.GITHUB.STATUS).then((res) => setStatus(res.data.data)).catch(() => {});
  }, []);

  const handleConnect = async () => {
    try {
      const res = await api.post(`${ENDPOINTS.GITHUB.CONNECT}?accessToken=${token}&username=${username}`);
      setStatus(res.data.data);
      toast.success("GitHub connected!");
    } catch {
      toast.error("Connection failed");
    }
  };

  return (
    <div>
      <div className="mb-8">
        <h1 className="text-3xl font-bold">GitHub Integration</h1>
        <p className="text-black">Connect repositories and track development</p>
      </div>

      <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
        <Card className="border-0 shadow-sm lg:col-span-1">
          <CardHeader>
            <CardTitle className="flex items-center gap-2"><Code2 size={20} /> Connection</CardTitle>
          </CardHeader>
          <CardContent className="space-y-4">
            {status?.connected ? (
              <div className="text-center py-4">
                <div className="h-12 w-12 rounded-full bg-emerald-100 flex items-center justify-center mx-auto mb-3">
                  <Code2 className="text-emerald-600" />
                </div>
                <p className="font-medium">Connected as @{status.username}</p>
                <p className="text-sm text-slate-400 mt-1">{status.repositories?.length || 0} repos linked</p>
              </div>
            ) : (
              <>
                <Input placeholder="GitHub Username" value={username} onChange={(e) => setUsername(e.target.value)} />
                <Input placeholder="Access Token" type="password" value={token} onChange={(e) => setToken(e.target.value)} />
                <Button className="w-full" onClick={handleConnect}><Link size={16} className="mr-2" /> Connect GitHub</Button>
              </>
            )}
          </CardContent>
        </Card>

        <div className="lg:col-span-2 space-y-6">
          <Card className="border-0 shadow-sm">
            <CardHeader><CardTitle className="flex items-center gap-2"><GitBranch size={18} /> Branches</CardTitle></CardHeader>
            <CardContent>
              <div className="space-y-2">
                {["main", "develop", "feature/auth"].map((branch) => (
                  <div key={branch} className="flex items-center justify-between p-3 bg-slate-50 rounded-lg">
                    <span className="font-mono text-sm">{branch}</span>
                    <span className="text-xs text-slate-400">Active</span>
                  </div>
                ))}
              </div>
            </CardContent>
          </Card>

          <Card className="border-0 shadow-sm">
            <CardHeader><CardTitle className="flex items-center gap-2"><GitPullRequest size={18} /> Pull Requests</CardTitle></CardHeader>
            <CardContent>
              <div className="space-y-2">
                {[
                  { title: "Add JWT authentication", state: "open", number: 1 },
                  { title: "Fix kanban drag-drop", state: "merged", number: 2 },
                ].map((pr) => (
                  <div key={pr.number} className="flex items-center justify-between p-3 bg-slate-50 rounded-lg">
                    <span className="text-sm">#{pr.number} {pr.title}</span>
                    <span className={`text-xs px-2 py-0.5 rounded-full ${pr.state === "open" ? "bg-green-100 text-green-700" : "bg-purple-100 text-purple-700"}`}>
                      {pr.state}
                    </span>
                  </div>
                ))}
              </div>
            </CardContent>
          </Card>
        </div>
      </div>
    </div>
  );
}
