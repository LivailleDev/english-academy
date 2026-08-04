import { apiFetch } from "./client";
import type { ExtraLesson } from "./types";

export function listExtraLessons(): Promise<ExtraLesson[]> {
  return apiFetch<ExtraLesson[]>("/extra-lessons");
}
