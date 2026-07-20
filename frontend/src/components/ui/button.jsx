import { Button as ButtonPrimitive } from "@base-ui/react/button";
import { cva } from "class-variance-authority";

import { cn } from "@/lib/utils";

const buttonVariants = cva(
  "group/button inline-flex shrink-0 items-center justify-center rounded-xl border border-transparent text-sm font-medium whitespace-nowrap transition-all duration-300 outline-none select-none focus-visible:border-blue-500 focus-visible:ring-4 focus-visible:ring-blue-200 active:not-aria-[haspopup]:translate-y-px disabled:pointer-events-none disabled:opacity-50 [&_svg]:pointer-events-none [&_svg]:shrink-0 [&_svg:not([class*='size-'])]:size-4",
  {
    variants: {
      variant: {
        default:
          "bg-gradient-to-r from-blue-600 to-indigo-600 text-white shadow-sm hover:from-blue-700 hover:to-indigo-700 hover:shadow-lg hover:-translate-y-0.5",

        outline:
          "border-slate-200 bg-white text-slate-700 shadow-sm hover:bg-slate-50 hover:border-blue-300 hover:text-blue-700 hover:shadow-md",

        secondary:
          "bg-slate-100 text-slate-700 hover:bg-slate-200 hover:shadow-sm",

        ghost:
          "text-slate-700 hover:bg-slate-100 hover:text-slate-900",

        destructive:
          "bg-red-500 text-white shadow-sm hover:bg-red-600 hover:shadow-lg",

        link:
          "text-blue-600 underline-offset-4 hover:underline",
      },

      size: {
        default:
          "h-10 gap-2 px-4",

        xs:
          "h-7 gap-1 rounded-lg px-2 text-xs [&_svg:not([class*='size-'])]:size-3",

        sm:
          "h-9 gap-2 rounded-lg px-3 text-sm [&_svg:not([class*='size-'])]:size-4",

        lg:
          "h-11 gap-2 px-6 text-base",

        icon:
          "size-10",

        "icon-xs":
          "size-7 rounded-lg [&_svg:not([class*='size-'])]:size-3",

        "icon-sm":
          "size-9 rounded-lg",

        "icon-lg":
          "size-11",
      },
    },

    defaultVariants: {
      variant: "default",
      size: "default",
    },
  }
);

function Button({
  className,
  variant = "default",
  size = "default",
  ...props
}) {
  return (
    <ButtonPrimitive
      data-slot="button"
      className={cn(buttonVariants({ variant, size, className }))}
      {...props}
    />
  );
}

export { Button, buttonVariants };