import type { LessonCategory } from "../api/types";

export const CATEGORY_BADGE_STYLES: Record<LessonCategory, string> = {
  GRAMMAR: "bg-indigo-100 text-indigo-700",
  VOCABULARY: "bg-emerald-100 text-emerald-700",
  PRONUNCIATION: "bg-rose-100 text-rose-700",
  IDIOMS: "bg-amber-100 text-amber-800",
  CULTURE: "bg-violet-100 text-violet-700",
};

export const CATEGORY_LABELS: Record<LessonCategory, string> = {
  GRAMMAR: "Grammar",
  VOCABULARY: "Vocabulary",
  PRONUNCIATION: "Pronunciation",
  IDIOMS: "Idioms",
  CULTURE: "Culture",
};
