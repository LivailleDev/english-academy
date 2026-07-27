import { apiFetch } from "./client";
import type { Enrollment, Student } from "./types";

export function createStudent(name: string, email: string): Promise<Student> {
  return apiFetch<Student>("/students", {
    method: "POST",
    body: JSON.stringify({ name, email }),
  });
}

export function enroll(studentId: number, courseId: number): Promise<Enrollment> {
  return apiFetch<Enrollment>("/enrollments", {
    method: "POST",
    body: JSON.stringify({ studentId, courseId }),
  });
}
