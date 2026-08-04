import { apiFetch } from "./client";
import type { StudyModule } from "./types";

export function listStudyModules(): Promise<StudyModule[]> {
  return apiFetch<StudyModule[]>("/study-modules");
}
