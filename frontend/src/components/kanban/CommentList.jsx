import { MessageCircle } from "lucide-react";

export default function CommentList({ comments = [] }) {
  if (!comments.length) {
    return (
      <div className="border rounded-lg p-6 text-center text-slate-500">
        <MessageCircle className="mx-auto mb-2" size={30} />
        <p>No comments yet</p>
      </div>
    );
  }

  return (
    <div className="space-y-3 max-h-72 overflow-y-auto">

      {comments.map((comment) => (
        <div
          key={comment.id}
          className="border rounded-lg p-3 bg-slate-50"
        >

          <div className="flex justify-between items-center">

            <h4 className="font-medium text-sm">
              {comment.authorName}
            </h4>

            <span className="text-xs text-slate-400">
              {new Date(comment.createdAt).toLocaleString()}
            </span>

          </div>

          <p className="text-sm text-slate-700 mt-2 whitespace-pre-wrap">
            {comment.content}
          </p>

        </div>
      ))}

    </div>
  );
}