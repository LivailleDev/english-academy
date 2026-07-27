import type { CourseLevel } from "../api/types";

const LEVEL_STYLES: Record<CourseLevel, string> = {
  A1: "bg-emerald-100 text-emerald-700",
  A2: "bg-emerald-100 text-emerald-700",
  B1: "bg-indigo-100 text-indigo-700",
  B2: "bg-indigo-100 text-indigo-700",
  C1: "bg-amber-100 text-amber-800",
  C2: "bg-amber-100 text-amber-800",
};

export function LevelBadge({ level }: { level: CourseLevel }) {
  return (
    <span className={`inline-flex items-center rounded-full px-2.5 py-0.5 text-xs font-semibold ${LEVEL_STYLES[level]}`}>
      {level}
    </span>
  );
}
