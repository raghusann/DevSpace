export default function AuthLayout({ children }) {
  return (
    <div className="relative min-h-screen overflow-hidden bg-[radial-gradient(circle_at_top_left,#bfdbfe_0%,transparent_30%),radial-gradient(circle_at_bottom_right,#dbeafe_0%,transparent_35%),linear-gradient(to_bottom_right,#f8fbff,#eef6ff,#f8fafc)]">
      {/* Background Glow */}
      <div className="pointer-events-none absolute inset-0">
        <div className="absolute -top-40 -left-40 h-[420px] w-[420px] rounded-full bg-sky-300/30 blur-[130px]" />

        <div className="absolute top-1/3 -right-32 h-[360px] w-[360px] rounded-full bg-cyan-300/25 blur-[120px]" />

        <div className="absolute bottom-0 left-1/3 h-[280px] w-[280px] rounded-full bg-indigo-200/20 blur-[100px]" />
      </div>

      <div className="relative z-10 flex min-h-screen items-center justify-center px-6">
        {children}
      </div>
    </div>
  );
}