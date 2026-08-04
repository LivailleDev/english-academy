import type { CourseLevel } from "../api/types";

export const LEVEL_BADGE_STYLES: Record<CourseLevel, string> = {
  A1: "bg-emerald-100 text-emerald-700",
  A2: "bg-emerald-100 text-emerald-700",
  B1: "bg-indigo-100 text-indigo-700",
  B2: "bg-indigo-100 text-indigo-700",
  C1: "bg-amber-100 text-amber-800",
  C2: "bg-amber-100 text-amber-800",
};

export const LEVEL_GRADIENTS: Record<CourseLevel, string> = {
  A1: "from-emerald-500 to-teal-600",
  A2: "from-emerald-500 to-teal-600",
  B1: "from-indigo-500 to-violet-600",
  B2: "from-indigo-500 to-violet-600",
  C1: "from-amber-500 to-rose-600",
  C2: "from-amber-500 to-rose-600",
};

export const LEVEL_LABELS: Record<CourseLevel, string> = {
  A1: "Beginner",
  A2: "Beginner",
  B1: "Intermediate",
  B2: "Intermediate",
  C1: "Advanced",
  C2: "Advanced",
};

export type Tier = "Beginner" | "Intermediate" | "Advanced";

export const TIERS: Tier[] = ["Beginner", "Intermediate", "Advanced"];

export const TIER_PILL_STYLES: Record<Tier, string> = {
  Beginner: "bg-emerald-600 text-white",
  Intermediate: "bg-indigo-600 text-white",
  Advanced: "bg-amber-600 text-white",
};
