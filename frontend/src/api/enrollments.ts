import { apiFetch } from "./client";
import type { Enrollment, Student } from "./types";

export function createStudent(name: string, email: string): Promise<Student> {
  return apiFetch<Student>("/students", {
    method: "POST",
    body: JSON.stringify({ name, email }),
  });
}

export function getStudent(studentId: number): Promise<Student> {
  return apiFetch<Student>(`/students/${studentId}`);
}

export async function findStudentByEmail(email: string): Promise<Student> {
  const [student] = await apiFetch<Student[]>(`/students?email=${encodeURIComponent(email)}`);
  return student;
}

export function enroll(studentId: number, courseId: number): Promise<Enrollment> {
  return apiFetch<Enrollment>("/enrollments", {
    method: "POST",
    body: JSON.stringify({ studentId, courseId }),
  });
}

export function listStudentEnrollments(studentId: number): Promise<Enrollment[]> {
  return apiFetch<Enrollment[]>(`/students/${studentId}/enrollments`);
}
