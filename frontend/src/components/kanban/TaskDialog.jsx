import { useEffect, useState } from "react";
import toast from "react-hot-toast";
import api from "@/api/axios";
import ENDPOINTS from "@/api/endpoints";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import CommentList from "./CommentList";

export default function TaskDialog({
  open,
  onClose,
  task,
  boardId,
  projectId,
  refreshBoard,
}) {
  const [form, setForm] = useState({
    title: "",
    description: "",
    priority: "MEDIUM",
    status: "TODO",
    dueDate: "",
    assigneeId: "",
  });

  const [comments, setComments] = useState([]);
  const [comment, setComment] = useState("");
  const [saving, setSaving] = useState(false);

  useEffect(() => {
    if (!task) return;

    setForm({
      title: task.title || "",
      description: task.description || "",
      priority: task.priority || "MEDIUM",
      status: task.status || "TODO",
      dueDate: task.dueDate || "",
      assigneeId: task.assigneeId || "",
    });

    loadComments();
  }, [task]);
    const loadComments = async () => {
    if (!task) return;

    try {
      const res = await api.get(
        `/api/projects/${projectId}/kanban/boards/${boardId}/tasks/${task.id}/comments`
      );

      setComments(res.data.data || []);
    } catch {
      setComments([]);
    }
  };

  const handleChange = (e) => {
    setForm({
      ...form,
      [e.target.name]: e.target.value,
    });
  };

  const updateTask = async () => {
    try {
      setSaving(true);

      await api.put(
        ENDPOINTS.KANBAN.TASK(projectId, boardId, task.id),
        form
      );

      toast.success("Task updated");

      refreshBoard();
      onClose();
    } catch {
      toast.error("Failed to update task");
    } finally {
      setSaving(false);
    }
  };
    const deleteTask = async () => {
    if (!window.confirm("Delete this task?")) return;

    try {
      await api.delete(
        ENDPOINTS.KANBAN.TASK(projectId, boardId, task.id)
      );

      toast.success("Task deleted");

      refreshBoard();
      onClose();
    } catch {
      toast.error("Failed to delete task");
    }
  };

  const addComment = async () => {
    if (!comment.trim()) return;

    try {
      await api.post(
        `/api/projects/${projectId}/kanban/boards/${boardId}/tasks/${task.id}/comments`,
        {
          content: comment,
        }
      );

      setComment("");
      loadComments();

      toast.success("Comment added");
    } catch {
      toast.error("Failed to add comment");
    }
  };

  if (!open || !task) return null;

  return (
    <div className="fixed inset-0 bg-black/40 flex items-center justify-center z-50">
      <div className="bg-white rounded-xl w-full max-w-2xl p-6 max-h-[90vh] overflow-y-auto">

        <div className="flex justify-between items-center mb-5">
          <h2 className="text-xl font-semibold">
            Edit Task
          </h2>

          <button
            onClick={onClose}
            className="text-2xl text-slate-500 hover:text-black"
          >
            ×
          </button>
        </div>
                <div className="space-y-4">

          <div>
            <label className="text-sm font-medium">
              Title
            </label>

            <Input
              name="title"
              value={form.title}
              onChange={handleChange}
            />
          </div>

          <div>
            <label className="text-sm font-medium">
              Description
            </label>

            <textarea
              name="description"
              rows={4}
              value={form.description}
              onChange={handleChange}
              className="w-full border rounded-lg p-3 mt-1"
            />
          </div>

          <div className="grid grid-cols-2 gap-4">

            <div>
              <label className="text-sm font-medium">
                Priority
              </label>

              <select
                name="priority"
                value={form.priority}
                onChange={handleChange}
                className="w-full border rounded-lg p-2 mt-1"
              >
                <option value="LOW">LOW</option>
                <option value="MEDIUM">MEDIUM</option>
                <option value="HIGH">HIGH</option>
              </select>
            </div>

            <div>
              <label className="text-sm font-medium">
                Status
              </label>

              <select
                name="status"
                value={form.status}
                onChange={handleChange}
                className="w-full border rounded-lg p-2 mt-1"
              >
                <option value="TODO">TODO</option>
                <option value="IN_PROGRESS">IN PROGRESS</option>
                <option value="TESTING">TESTING</option>
                <option value="DONE">DONE</option>
              </select>
            </div>

          </div>

          <div className="grid grid-cols-2 gap-4">

            <div>
              <label className="text-sm font-medium">
                Due Date
              </label>

              <Input
                type="date"
                name="dueDate"
                value={form.dueDate}
                onChange={handleChange}
              />
            </div>

            <div>
              <label className="text-sm font-medium">
                Assignee Id
              </label>

              <Input
                name="assigneeId"
                value={form.assigneeId}
                onChange={handleChange}
                placeholder="User Id"
              />
            </div>

          </div>

          <div className="flex gap-3 mt-4">

            <Button
              onClick={updateTask}
              disabled={saving}
            >
              Save Changes
            </Button>

            <Button
              variant="destructive"
              onClick={deleteTask}
            >
              Delete Task
            </Button>

            <Button
              variant="outline"
              onClick={onClose}
            >
              Cancel
            </Button>

          </div>

          <hr className="my-5" />

          <h3 className="font-semibold">
            Comments
          </h3>

          <CommentList comments={comments} />

          <div className="flex gap-2 mt-3">

            <Input
              placeholder="Write a comment..."
              value={comment}
              onChange={(e) => setComment(e.target.value)}
            />

            <Button onClick={addComment}>
              Send
            </Button>

          </div>

        </div>

      </div>
    </div>
  );
}
