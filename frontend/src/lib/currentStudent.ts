const STORAGE_KEY = "englishAcademy.currentStudentId";

/**
 * No auth system exists yet, so "who's logged in" is simulated by
 * remembering the last student created via the enrollment form.
 */
export function getCurrentStudentId(): number | null {
  const raw = localStorage.getItem(STORAGE_KEY);
  return raw ? Number(raw) : null;
}

export function setCurrentStudentId(studentId: number): void {
  localStorage.setItem(STORAGE_KEY, String(studentId));
}
