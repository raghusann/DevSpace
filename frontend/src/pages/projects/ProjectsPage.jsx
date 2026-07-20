import { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import { Plus, Users, MoreHorizontal } from "lucide-react";
import toast from "react-hot-toast";
import { Button } from "@/components/ui/button";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Input } from "@/components/ui/input";
import api from "@/api/axios";
import ENDPOINTS from "@/api/endpoints";

export default function ProjectsPage() {
  const [projects, setProjects] = useState([]);
  const [showForm, setShowForm] = useState(false);
  const [form, setForm] = useState({
    name: "",
    description: "",
    key: "",
  });
  const [loading, setLoading] = useState(true);

  const fetchProjects = () => {
    api
      .get(ENDPOINTS.PROJECTS.BASE)
      .then((res) => setProjects(res.data.data?.content || []))
      .catch(() => toast.error("Failed to load projects"))
      .finally(() => setLoading(false));
  };

  useEffect(() => {
    fetchProjects();
  }, []);

  const handleCreate = async (e) => {
    e.preventDefault();

    try {
      const payload = {
        ...form,
        key: form.key.trim().toUpperCase(),
      };

      await api.post(ENDPOINTS.PROJECTS.BASE, payload);

      toast.success("Project created!");

      setShowForm(false);
      setForm({
        name: "",
        description: "",
        key: "",
      });

      fetchProjects();
    } catch (err) {
      console.error(err.response);

      const errors = err.response?.data?.data;

      if (errors && typeof errors === "object") {
        Object.values(errors).forEach((message) => {
          toast.error(message);
        });
      } else {
        toast.error(
          err.response?.data?.message || "Failed to create project"
        );
      }
    }
  };

  return (
    <div>
      <div className="flex items-center justify-between mb-8">
        <div>
          <h1 className="text-3xl font-bold">Projects</h1>
          <p className="text-black">Manage your development projects</p>
        </div>

        <Button onClick={() => setShowForm(!showForm)}>
          <Plus size={18} className="mr-2" />
          New Project
        </Button>
      </div>

      {showForm && (
        <Card className="mb-6 border-0 shadow-sm">
          <CardContent className="p-6">
            <form
              onSubmit={handleCreate}
              className="grid grid-cols-1 md:grid-cols-3 gap-4"
            >
              <Input
                placeholder="Project Name"
                value={form.name}
                onChange={(e) =>
                  setForm({ ...form, name: e.target.value })
                }
                required
              />

              <Input
                placeholder="Project Key (e.g. DEV)"
                value={form.key}
                onChange={(e) =>
                  setForm({ ...form, key: e.target.value })
                }
                required
              />

              <Input
                placeholder="Description"
                value={form.description}
                onChange={(e) =>
                  setForm({
                    ...form,
                    description: e.target.value,
                  })
                }
              />

              <Button type="submit" className="md:col-span-3 w-fit">
                Create Project
              </Button>
            </form>
          </CardContent>
        </Card>
      )}

      {loading ? (
        <p className="text-slate-400">Loading projects...</p>
      ) : projects.length === 0 ? (
        <Card className="border-0 shadow-sm">
          <CardContent className="p-12 text-center">
            <p className="text-slate-400 mb-4">
              No projects yet. Create your first project!
            </p>

            <Button onClick={() => setShowForm(true)}>
              <Plus size={18} className="mr-2" />
              Create Project
            </Button>
          </CardContent>
        </Card>
      ) : (
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
          {projects.map((project) => (
            <Card
              key={project.id}
              className="border-0 shadow-sm hover:shadow-md transition-shadow"
            >
              <CardHeader className="flex flex-row items-start justify-between">
                <div>
                  <span className="text-xs font-mono bg-slate-100 px-2 py-0.5 rounded">
                    {project.key}
                  </span>

                  <CardTitle className="mt-2 text-lg">
                    {project.name}
                  </CardTitle>
                </div>

                <MoreHorizontal
                  size={18}
                  className="text-slate-400"
                />
              </CardHeader>

              <CardContent>
                <p className="text-sm text-slate-500 mb-4 line-clamp-2">
                  {project.description || "No description"}
                </p>

                <div className="flex items-center justify-between">
                  <div className="flex items-center gap-1 text-sm text-slate-400">
                    <Users size={14} />
                    <span>{project.memberCount || 1} members</span>
                  </div>

                  <Link
                    to={`/projects/${project.id}/kanban`}
                    className="text-sm font-medium text-slate-900 hover:underline"
                  >
                    Open Board →
                  </Link>
                </div>
              </CardContent>
            </Card>
          ))}
        </div>
      )}
    </div>
  );
}