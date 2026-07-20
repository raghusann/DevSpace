import { Calendar, Flag, MessageSquare } from "lucide-react";

const PRIORITY_COLORS = {
  LOW: "bg-green-100 text-green-700",
  MEDIUM: "bg-yellow-100 text-yellow-700",
  HIGH: "bg-red-100 text-red-700",
};

export default function TaskCard({
  task,
  dragHandleProps,
  draggableProps,
  innerRef,
  onClick,
}) {
  return (
    <div
      ref={innerRef}
      {...draggableProps}
      {...dragHandleProps}
      onClick={() => onClick(task)}
      className="bg-white rounded-lg border border-slate-200 shadow-sm hover:shadow-md transition-all cursor-pointer p-3"
    >
      <div className="flex justify-between items-start gap-2">
        <h4 className="font-medium text-sm text-slate-800">
          {task.title}
        </h4>

        {task.priority && (
          <span
            className={`text-[10px] px-2 py-1 rounded-full font-medium ${
              PRIORITY_COLORS[task.priority] ||
              "bg-slate-100 text-slate-700"
            }`}
          >
            {task.priority}
          </span>
        )}
      </div>

      {task.description && (
        <p className="text-xs text-slate-500 mt-2 line-clamp-2">
          {task.description}
        </p>
      )}

      <div className="flex justify-between items-center mt-3 text-xs text-slate-400">

        {task.dueDate ? (
          <div className="flex items-center gap-1">
            <Calendar size={13} />
            {new Date(task.dueDate).toLocaleDateString()}
          </div>
        ) : (
          <span />
        )}

        <div className="flex items-center gap-3">

          {task.comments && (
            <div className="flex items-center gap-1">
              <MessageSquare size={13} />
              {task.comments.length}
            </div>
          )}

          <div className="flex items-center gap-1">
            <Flag size={13} />
            {task.position}
          </div>

        </div>
      </div>
    </div>
  );
}