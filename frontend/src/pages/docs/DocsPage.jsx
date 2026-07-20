import { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import { Plus, Search, FileText, Pencil, Trash2, X } from "lucide-react";
import toast from "react-hot-toast";

import { Button } from "@/components/ui/button";
import { Card, CardContent, CardHeader } from "@/components/ui/card";
import { Input } from "@/components/ui/input";
import { Textarea } from "@/components/ui/textarea";

import api from "@/api/axios";
import ENDPOINTS from "@/api/endpoints";

export default function DocsPage() {
  const { projectId } = useParams();

  const [docs, setDocs] = useState([]);
  const [selected, setSelected] = useState(null);

  const [loading, setLoading] = useState(false);

  const [search, setSearch] = useState("");

  const [showForm, setShowForm] = useState(false);

  const [editing, setEditing] = useState(false);

  const [form, setForm] = useState({
    title: "",
    content: "",
  });

  const fetchDocs = async () => {
    if (!projectId) return;

    try {
      setLoading(true);

      const params = search ? `?search=${encodeURIComponent(search)}` : "";

      const res = await api.get(
        `${ENDPOINTS.DOCUMENTS.BASE(projectId)}${params}`,
      );

      const list = res.data.data?.content || [];

      setDocs(list);

      if (list.length) {
        if (!selected) {
          setSelected(list[0]);
        } else {
          const current = list.find((d) => d.id === selected.id);
          setSelected(current || list[0]);
        }
      } else {
        setSelected(null);
      }
    } catch {
      toast.error("Failed to load documents");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    if (projectId) {
      fetchDocs();
    }
  }, [projectId, search]);

  const resetForm = () => {
    setEditing(false);
    setShowForm(false);

    setForm({
      title: "",
      content: "",
    });
  };

  const handleCreateOrUpdate = async () => {
    if (!form.title.trim()) {
      toast.error("Title is required");
      return;
    }

    try {
      if (editing && selected) {
        await api.put(
          `${ENDPOINTS.DOCUMENTS.BASE(projectId)}/${selected.id}`,
          form,
        );

        setSelected({
          ...selected,
          title: form.title,
          content: form.content,
        });

        toast.success("Document updated");
      } else {
        await api.post(ENDPOINTS.DOCUMENTS.BASE(projectId), form);

        toast.success("Document created");
      }

      resetForm();
      fetchDocs();
    } catch {
      toast.error("Operation failed");
    }
  };

  const handleEdit = () => {
    if (!selected) return;

    setEditing(true);

    setForm({
      title: selected.title,
      content: selected.content,
    });

    setShowForm(true);
  };

  const handleDelete = async () => {
    if (!selected) return;

    if (!window.confirm("Delete this document?")) return;

    try {
      await api.delete(`${ENDPOINTS.DOCUMENTS.BASE(projectId)}/${selected.id}`);

      toast.success("Document deleted");

      setSelected(null);
      resetForm();

      fetchDocs();
    } catch {
      toast.error("Delete failed");
    }
  };
  return (
    <div className="space-y-6">
      <div className="flex items-center justify-between">
        <div>
          <h1 className="text-3xl font-bold">Documentation</h1>
          <p className="text-slate-500">
            Project documents, API notes and guides
          </p>
        </div>

        <Button
          onClick={() => {
            resetForm();
            setShowForm(true);
          }}
        >
          <Plus size={16} className="mr-2" />
          New Document
        </Button>
      </div>

      {showForm && (
        <Card>
          <CardHeader className="flex flex-row items-center justify-between">
            <h2 className="font-semibold text-lg">
              {editing ? "Edit Document" : "Create Document"}
            </h2>

            <Button variant="ghost" size="icon" onClick={resetForm}>
              <X size={18} />
            </Button>
          </CardHeader>

          <CardContent className="space-y-4">
            <Input
              placeholder="Document title"
              value={form.title}
              onChange={(e) =>
                setForm({
                  ...form,
                  title: e.target.value,
                })
              }
            />

            <Textarea
              rows={12}
              placeholder="Write your documentation..."
              value={form.content}
              onChange={(e) =>
                setForm({
                  ...form,
                  content: e.target.value,
                })
              }
            />

            <div className="flex gap-2">
              <Button onClick={handleCreateOrUpdate}>
                {editing ? "Update Document" : "Create Document"}
              </Button>

              <Button variant="outline" onClick={resetForm}>
                Cancel
              </Button>
            </div>
          </CardContent>
        </Card>
      )}

      <div className="grid lg:grid-cols-3 gap-6">
        <Card>
          <CardHeader>
            <div className="relative">
              <Search
                size={16}
                className="absolute left-3 top-1/2 -translate-y-1/2 text-slate-400"
              />

              <Input
                className="pl-9"
                placeholder="Search documents..."
                value={search}
                onChange={(e) => setSearch(e.target.value)}
              />
            </div>
          </CardHeader>

          <CardContent className="space-y-2">
            {loading ? (
              <p className="text-center text-sm text-slate-500">Loading...</p>
            ) : docs.length === 0 ? (
              <p className="text-center text-sm text-slate-500">
                No documents found.
              </p>
            ) : (
              docs.map((doc) => (
                <button
                  key={doc.id}
                  onClick={() => setSelected(doc)}
                  className={`w-full rounded-lg border px-3 py-3 text-left transition ${
                    selected?.id === doc.id
                      ? "bg-slate-900 text-white"
                      : "hover:bg-slate-100"
                  }`}
                >
                  <div className="flex items-center gap-2">
                    <FileText size={16} />
                    <span className="font-medium truncate">{doc.title}</span>
                  </div>
                </button>
              ))
            )}
          </CardContent>
        </Card>
        <Card className="lg:col-span-2">
          <CardContent className="p-8">
            {selected ? (
              <>
                <div className="flex items-start justify-between mb-6">
                  <div>
                    <h2 className="text-3xl font-bold">{selected.title}</h2>

                    <p className="text-sm text-slate-500 mt-1">
                      Document ID : {selected.id}
                    </p>
                  </div>

                  <div className="flex gap-2">
                    <Button variant="outline" onClick={handleEdit}>
                      <Pencil size={16} className="mr-2" />
                      Edit
                    </Button>

                    <Button variant="destructive" onClick={handleDelete}>
                      <Trash2 size={16} className="mr-2" />
                      Delete
                    </Button>
                  </div>
                </div>

                <div className="rounded-lg border bg-slate-50 p-6">
                  <pre className="whitespace-pre-wrap break-words text-sm leading-7 font-sans">
                    {selected.content}
                  </pre>
                </div>
              </>
            ) : (
              <div className="flex h-[400px] items-center justify-center">
                <div className="text-center">
                  <FileText size={60} className="mx-auto text-slate-300 mb-4" />

                  <h3 className="text-lg font-semibold">
                    No Document Selected
                  </h3>

                  <p className="text-slate-500 mt-2">
                    Select a document from the left or create a new one.
                  </p>
                </div>
              </div>
            )}
          </CardContent>
        </Card>
      </div>
    </div>
  );
}
