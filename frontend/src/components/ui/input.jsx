import * as React from "react";
import { Input as InputPrimitive } from "@base-ui/react/input";

import { cn } from "@/lib/utils";

function Input({
  className,
  type,
  ...props
}) {
  return (
    <InputPrimitive
      type={type}
      data-slot="input"
      className={cn(
        "h-10 w-full min-w-0 rounded-xl border border-slate-300 bg-white/90 px-4 text-sm text-slate-900 shadow-sm transition-all duration-300 outline-none placeholder:text-slate-400 file:inline-flex file:h-6 file:border-0 file:bg-transparent file:text-sm file:font-medium file:text-slate-700 hover:border-slate-400 focus-visible:border-blue-500 focus-visible:ring-4 focus-visible:ring-blue-100 disabled:pointer-events-none disabled:cursor-not-allowed disabled:bg-slate-100 disabled:opacity-60 aria-invalid:border-red-500 aria-invalid:ring-4 aria-invalid:ring-red-100 dark:bg-input/30 dark:disabled:bg-input/80",
        className
      )}
      {...props}
    />
  );
}

export { Input };