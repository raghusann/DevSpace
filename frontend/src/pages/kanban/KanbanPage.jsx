import { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import { DragDropContext, Droppable, Draggable } from "@hello-pangea/dnd";
import { Plus } from "lucide-react";
import toast from "react-hot-toast";

import { Button } from "@/components/ui/button";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Input } from "@/components/ui/input";

import api from "@/api/axios";
import ENDPOINTS from "@/api/endpoints";

import TaskCard from "@/components/kanban/TaskCard";
import TaskDialog from "@/components/kanban/TaskDialog";

const COLUMN_COLORS = {
  TODO: "border-t-slate-400",
  IN_PROGRESS: "border-t-blue-500",
  TESTING: "border-t-amber-500",
  DONE: "border-t-emerald-500",
};

export default function KanbanPage() {
  const { projectId } = useParams();

  const [board, setBoard] = useState(null);
  const [newTask, setNewTask] = useState("");
  const [activeColumn, setActiveColumn] = useState(null);

  const [selectedTask, setSelectedTask] = useState(null);
  const [dialogOpen, setDialogOpen] = useState(false);

  const fetchBoard = async () => {
    try {
      const boardsRes = await api.get(ENDPOINTS.KANBAN.BOARDS(projectId));

      let boards = boardsRes.data.data || [];

      if (boards.length === 0) {
        const createRes = await api.post(ENDPOINTS.KANBAN.BOARDS(projectId), {
          name: "Main Board",
        });

        boards = [createRes.data.data];
      }

      const boardRes = await api.get(
        ENDPOINTS.KANBAN.BOARD(projectId, boards[0].id),
      );

      setBoard(boardRes.data.data);
    } catch {
      toast.error("Failed to load kanban board");
    }
  };

  useEffect(() => {
    fetchBoard();
  }, [projectId]);

  const openTask = (task) => {
    setSelectedTask(task);
    setDialogOpen(true);
  };

  const closeTask = () => {
    setDialogOpen(false);
    setSelectedTask(null);
  };
  const onDragEnd = async (result) => {
    if (!result.destination || !board) return;

    const { draggableId, destination } = result;

    const targetColumn = board.columns.find(
      (c) => c.id.toString() === destination.droppableId,
    );

    if (!targetColumn) return;

    try {
      await api.put(
        ENDPOINTS.KANBAN.MOVE_TASK(projectId, board.id, draggableId),
        {
          targetColumnId: targetColumn.id,
          position: destination.index,
        },
      );

      fetchBoard();
    } catch {
      toast.error("Failed to move task");
    }
  };

  const addTask = async (columnId) => {
    if (!newTask.trim() || !board) return;

    try {
      await api.post(ENDPOINTS.KANBAN.TASKS(projectId, board.id, columnId), {
        title: newTask,
        priority: "MEDIUM",
      });

      toast.success("Task created");

      setNewTask("");
      setActiveColumn(null);

      fetchBoard();
    } catch {
      toast.error("Failed to create task");
    }
  };

  const createColumn = async () => {
    const name = prompt("Column name");

    if (!name || !board) return;

    try {
      await api.post(ENDPOINTS.KANBAN.COLUMNS(projectId, board.id), {
        name,
      });

      toast.success("Column created");
      fetchBoard();
    } catch {
      toast.error("Failed to create column");
    }
  };

  const renameColumn = async (column) => {
    const name = prompt("Enter new column name", column.name);

    if (!name || name === column.name) return;

    try {
      await api.put(
        ENDPOINTS.KANBAN.UPDATE_COLUMN(projectId, board.id, column.id),
        {
          name,
          position: column.position,
          wipLimit: column.wipLimit,
        },
      );

      toast.success("Column updated");
      fetchBoard();
    } catch {
      toast.error("Failed to update column");
    }
  };

  const deleteColumn = async (column) => {
    if (!window.confirm(`Delete "${column.name}" column?`)) return;

    try {
      await api.delete(
        ENDPOINTS.KANBAN.DELETE_COLUMN(projectId, board.id, column.id),
      );

      toast.success("Column deleted");
      fetchBoard();
    } catch (err) {
      toast.error(err.response?.data?.message || "Failed to delete column");
    }
  };

  if (!board) {
    return <p className="text-slate-400">Loading board...</p>;
  }

  return (
    <div>
      <div className="mb-6">
        <h1 className="text-2xl font-bold">{board.name}</h1>

        <p className="text-slate-500">Drag and drop tasks between columns</p>
      </div>

      <DragDropContext onDragEnd={onDragEnd}>
        <div className="flex gap-4 overflow-x-auto pb-4">
          {board.columns?.map((column) => (
            <div key={column.id} className="min-w-[280px] flex-shrink-0">
              <Card
                className={`border-0 shadow-sm border-t-4 ${
                  COLUMN_COLORS[
                    column.name?.toUpperCase()?.replace(" ", "_")
                  ] || "border-t-slate-300"
                }`}
              >
                <CardHeader className="pb-2">
                  <CardTitle className="flex items-center justify-between text-sm">
                    <div className="flex items-center gap-2">
                      <span>{column.name}</span>

                      <button
                        className="text-blue-600 hover:text-blue-800 text-xs"
                        onClick={() => renameColumn(column)}
                      >
                        ✏️
                      </button>

                      <button
                        className="text-red-600 hover:text-red-800 text-xs"
                        onClick={() => deleteColumn(column)}
                      >
                        🗑️
                      </button>
                    </div>

                    <span className="bg-slate-100 px-2 py-0.5 rounded-full text-xs">
                      {column.tasks?.length || 0}
                    </span>
                  </CardTitle>
                </CardHeader>

                <CardContent>
                  <Droppable droppableId={column.id.toString()}>
                    {(provided) => (
                      <div
                        ref={provided.innerRef}
                        {...provided.droppableProps}
                        className="space-y-2 min-h-[100px]"
                      >
                        {column.tasks?.map((task, index) => (
                          <Draggable
                            key={task.id}
                            draggableId={task.id.toString()}
                            index={index}
                          >
                            {(prov) => (
                              <TaskCard
                                task={task}
                                innerRef={prov.innerRef}
                                draggableProps={prov.draggableProps}
                                dragHandleProps={prov.dragHandleProps}
                                onClick={openTask}
                              />
                            )}
                          </Draggable>
                        ))}
                        {provided.placeholder}
                      </div>
                    )}
                  </Droppable>

                  {activeColumn === column.id ? (
                    <div className="mt-2 space-y-2">
                      <Input
                        placeholder="Task title..."
                        value={newTask}
                        onChange={(e) => setNewTask(e.target.value)}
                        onKeyDown={(e) => {
                          if (e.key === "Enter") {
                            addTask(column.id);
                          }
                        }}
                        autoFocus
                      />
                      <div className="flex gap-2">
                        <Button size="sm" onClick={() => addTask(column.id)}>
                          Add
                        </Button>

                        <Button
                          size="sm"
                          variant="outline"
                          onClick={() => setActiveColumn(null)}
                        >
                          Cancel
                        </Button>
                      </div>
                    </div>
                  ) : (
                    <button
                      onClick={() => setActiveColumn(column.id)}
                      className="flex items-center gap-1 mt-2 text-sm text-slate-400 hover:text-slate-600"
                    >
                      <Plus size={14} />
                      Add task
                    </button>
                  )}
                </CardContent>
              </Card>
            </div>
          ))}
          <div className="min-w-[280px] flex-shrink-0">
            <Card className="border-2 border-dashed border-slate-300 shadow-none h-full">
              <CardContent className="flex items-center justify-center py-8">
                <Button onClick={createColumn}>+ Add Column</Button>
              </CardContent>
            </Card>
          </div>
        </div>
      </DragDropContext>

      <TaskDialog
        open={dialogOpen}
        task={selectedTask}
        onClose={closeTask}
        projectId={projectId}
        boardId={board.id}
        refreshBoard={fetchBoard}
      />
    </div>
  );
}
