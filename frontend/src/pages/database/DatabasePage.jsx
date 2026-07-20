import { useState } from "react";
import { Database, Play } from "lucide-react";
import toast from "react-hot-toast";
import { Button } from "@/components/ui/button";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Input } from "@/components/ui/input";
import api from "@/api/axios";
import ENDPOINTS from "@/api/endpoints";

export default function DatabasePage() {
  const [projectId] = useState("1");
  const [tables, setTables] = useState([]);
  const [query, setQuery] = useState("SELECT * FROM users LIMIT 10");
  const [result, setResult] = useState(null);
  const [config, setConfig] = useState({
    host: "localhost", port: 5432, databaseName: "devspace", username: "devspace", password: "devspace123",
  });

  const saveConfig = async () => {
    try {
      await api.put(ENDPOINTS.DATABASE.CONFIG(projectId), config);
      toast.success("Database config saved");
    } catch {
      toast.error("Failed to save config");
    }
  };

  const loadTables = async () => {
    try {
      const res = await api.get(ENDPOINTS.DATABASE.TABLES(projectId));
      setTables(res.data.data || []);
    } catch {
      toast.error("Failed to load tables. Save config first.");
    }
  };

  const runQuery = async () => {
    try {
      const res = await api.post(ENDPOINTS.DATABASE.QUERY(projectId), { query });
      setResult(res.data.data);
    } catch (err) {
      toast.error(err.response?.data?.message || "Query failed");
    }
  };

  return (
    <div>
      <div className="mb-8">
        <h1 className="text-3xl font-bold">Database Explorer</h1>
        <p className="text-black">Browse tables and run SQL queries</p>
      </div>

      <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
        <Card className="border-0 shadow-sm">
          <CardHeader><CardTitle className="text-sm flex items-center gap-2"><Database size={16} /> Connection</CardTitle></CardHeader>
          <CardContent className="space-y-3">
            {Object.entries(config).map(([key, val]) => (
              <div key={key}>
                <label className="text-xs text-slate-500 capitalize">{key}</label>
                <Input
                  type={key === "password" ? "password" : key === "port" ? "number" : "text"}
                  value={val}
                  onChange={(e) => setConfig({ ...config, [key]: key === "port" ? parseInt(e.target.value) : e.target.value })}
                  className="mt-1"
                />
              </div>
            ))}
            <Button className="w-full" onClick={saveConfig}>Save Config</Button>
            <Button variant="outline" className="w-full" onClick={loadTables}>Load Tables</Button>
          </CardContent>
        </Card>

        <div className="lg:col-span-2 space-y-4">
          {tables.length > 0 && (
            <Card className="border-0 shadow-sm">
              <CardHeader><CardTitle className="text-sm">Tables ({tables.length})</CardTitle></CardHeader>
              <CardContent className="flex flex-wrap gap-2">
                {tables.map((t) => (
                  <button
                    key={t.name}
                    onClick={() => setQuery(`SELECT * FROM ${t.name} LIMIT 10`)}
                    className="px-3 py-1.5 bg-slate-100 hover:bg-slate-200 rounded-lg text-sm font-mono"
                  >
                    {t.name}
                  </button>
                ))}
              </CardContent>
            </Card>
          )}

          <Card className="border-0 shadow-sm">
            <CardHeader><CardTitle className="text-sm">SQL Runner</CardTitle></CardHeader>
            <CardContent>
              <textarea
                value={query}
                onChange={(e) => setQuery(e.target.value)}
                className="w-full h-24 font-mono text-sm border rounded-lg p-3 resize-none mb-3"
              />
              <Button onClick={runQuery}><Play size={16} className="mr-2" /> Run Query</Button>
            </CardContent>
          </Card>

          {result && (
            <Card className="border-0 shadow-sm">
              <CardHeader><CardTitle className="text-sm">Results ({result.rowCount} rows)</CardTitle></CardHeader>
              <CardContent className="overflow-x-auto">
                <table className="w-full text-sm">
                  <thead>
                    <tr className="border-b">
                      {result.columns?.map((col) => <th key={col} className="text-left p-2 font-medium">{col}</th>)}
                    </tr>
                  </thead>
                  <tbody>
                    {result.rows?.map((row, i) => (
                      <tr key={i} className="border-b">
                        {result.columns?.map((col) => <td key={col} className="p-2 font-mono text-xs">{String(row[col] ?? "")}</td>)}
                      </tr>
                    ))}
                  </tbody>
                </table>
              </CardContent>
            </Card>
          )}
        </div>
      </div>
    </div>
  );
}
