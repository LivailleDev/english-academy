import { apiFetch } from "./client";
import type { Course } from "./types";

export function listCourses(): Promise<Course[]> {
  return apiFetch<Course[]>("/courses");
}

export function getCourse(id: number): Promise<Course> {
  return apiFetch<Course>(`/courses/${id}`);
}
