import { useEffect, useState } from "react";
import { Send, Plus } from "lucide-react";
import toast from "react-hot-toast";
import { Button } from "@/components/ui/button";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Input } from "@/components/ui/input";
import api from "@/api/axios";
import ENDPOINTS from "@/api/endpoints";

const METHODS = ["GET", "POST", "PUT", "DELETE", "PATCH"];

export default function ApiTesterPage() {
  const [method, setMethod] = useState("GET");
  const [url, setUrl] = useState("https://jsonplaceholder.typicode.com/posts/1");
  const [headers, setHeaders] = useState('{"Content-Type": "application/json"}');
  const [body, setBody] = useState("");
  const [response, setResponse] = useState(null);
  const [loading, setLoading] = useState(false);
  const [collections, setCollections] = useState([]);

  const sendRequest = async () => {
    setLoading(true);
    try {
      let parsedHeaders = {};
      try { parsedHeaders = JSON.parse(headers); } catch { /* ignore */ }

      const res = await api.post(ENDPOINTS.API_TESTER.PROXY, {
        method, url, headers: parsedHeaders, body: body || null,
      });
      setResponse(res.data.data);
    } catch (err) {
      setResponse({ status: err.response?.status || 500, body: err.response?.data?.message || "Request failed" });
      toast.error("Request failed");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => { loadCollections(); }, []);

  const loadCollections = async () => {
    try {
      const res = await api.get(ENDPOINTS.API_TESTER.COLLECTIONS);
      setCollections(res.data.data || []);
    } catch { /* ignore */ }
  };

  return (
    <div>
      <div className="mb-6">
        <h1 className="text-3xl font-bold">API Tester</h1>
        <p className="text-black">Test REST APIs like Postman</p>
      </div>

      <div className="grid grid-cols-1 lg:grid-cols-4 gap-6">
        <Card className="border-0 shadow-sm">
          <CardHeader className="flex flex-row items-center justify-between">
            <CardTitle className="text-sm">Collections</CardTitle>
            <Button size="sm" variant="outline"><Plus size={14} /></Button>
          </CardHeader>
          <CardContent>
            {collections.length === 0 ? (
              <p className="text-sm text-slate-400">No collections yet</p>
            ) : (
              collections.map((c) => (
                <div key={c.id} className="text-sm py-1.5 px-2 hover:bg-slate-50 rounded cursor-pointer">{c.name}</div>
              ))
            )}
          </CardContent>
        </Card>

        <div className="lg:col-span-3 space-y-4">
          <Card className="border-0 shadow-sm">
            <CardContent className="p-4">
              <div className="flex gap-2">
                <select
                  value={method}
                  onChange={(e) => setMethod(e.target.value)}
                  className="border rounded-lg px-3 py-2 text-sm font-mono font-medium bg-slate-50"
                >
                  {METHODS.map((m) => <option key={m}>{m}</option>)}
                </select>
                <Input value={url} onChange={(e) => setUrl(e.target.value)} className="flex-1 font-mono text-sm" />
                <Button onClick={sendRequest} disabled={loading}>
                  <Send size={16} className="mr-2" />{loading ? "Sending..." : "Send"}
                </Button>
              </div>
            </CardContent>
          </Card>

          <div className="grid grid-cols-2 gap-4">
            <Card className="border-0 shadow-sm">
              <CardHeader><CardTitle className="text-sm">Headers</CardTitle></CardHeader>
              <CardContent>
                <textarea
                  value={headers}
                  onChange={(e) => setHeaders(e.target.value)}
                  className="w-full h-32 font-mono text-sm border rounded-lg p-3 resize-none"
                />
              </CardContent>
            </Card>
            <Card className="border-0 shadow-sm">
              <CardHeader><CardTitle className="text-sm">Body</CardTitle></CardHeader>
              <CardContent>
                <textarea
                  value={body}
                  onChange={(e) => setBody(e.target.value)}
                  className="w-full h-32 font-mono text-sm border rounded-lg p-3 resize-none"
                  placeholder='{"key": "value"}'
                />
              </CardContent>
            </Card>
          </div>

          {response && (
            <Card className="border-0 shadow-sm">
              <CardHeader>
                <CardTitle className="text-sm">
                  Response {response.status && <span className="ml-2 text-emerald-600">{response.status}</span>}
                </CardTitle>
              </CardHeader>
              <CardContent>
                <pre className="bg-slate-950 text-green-400 p-4 rounded-lg text-sm overflow-auto max-h-96 font-mono">
                  {typeof response.body === "object" ? JSON.stringify(response.body, null, 2) : String(response.body)}
                </pre>
              </CardContent>
            </Card>
          )}
        </div>
      </div>
    </div>
  );
}
