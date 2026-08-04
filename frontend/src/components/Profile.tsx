import { useEffect, useState } from "react";
import { getStudent, listStudentEnrollments } from "../api/enrollments";
import type { Enrollment, Student } from "../api/types";
import { getCurrentStudentId } from "../lib/currentStudent";

interface Props {
  onBrowseCourses: () => void;
}

export function Profile({ onBrowseCourses }: Props) {
  const [student, setStudent] = useState<Student | null>(null);
  const [enrollments, setEnrollments] = useState<Enrollment[]>([]);
  const [loading, setLoading] = useState(true);
  const studentId = getCurrentStudentId();

  useEffect(() => {
    if (studentId === null) {
      setLoading(false);
      return;
    }
    Promise.all([getStudent(studentId), listStudentEnrollments(studentId)]).then(([s, e]) => {
      setStudent(s);
      setEnrollments(e);
      setLoading(false);
    });
  }, [studentId]);

  if (loading) return <p className="text-stone-500">Loading profile…</p>;

  if (!student) {
    return (
      <div className="animate-fade-in-up rounded-xl border border-stone-200 bg-white p-8 text-center">
        <h2 className="mb-2 font-serif text-xl font-semibold text-stone-900">No profile yet</h2>
        <p className="mb-4 text-sm text-stone-500">
          Enroll in a course and your profile will show up here — this demo doesn't have login, so it remembers
          the last student who enrolled on this browser.
        </p>
        <button
          onClick={onBrowseCourses}
          className="rounded-md bg-indigo-600 px-4 py-2 text-sm font-semibold text-white transition hover:bg-indigo-700"
        >
          Browse courses
        </button>
      </div>
    );
  }

  return (
    <div className="animate-fade-in-up">
      <div className="mb-8 flex items-center gap-4 rounded-2xl bg-gradient-to-br from-rose-500 to-indigo-600 px-6 py-8 text-white shadow-md">
        <div className="flex h-16 w-16 shrink-0 items-center justify-center rounded-full bg-white/20 font-serif text-2xl font-semibold">
          {student.name.charAt(0).toUpperCase()}
        </div>
        <div>
          <h2 className="font-serif text-2xl font-semibold">{student.name}</h2>
          <p className="text-sm text-white/85">{student.email}</p>
        </div>
      </div>

      <h3 className="mb-3 font-serif text-lg font-semibold text-stone-900">My enrollments</h3>
      {enrollments.length === 0 ? (
        <p className="text-sm text-stone-500">No enrollments yet.</p>
      ) : (
        <ul className="space-y-3">
          {enrollments.map((enrollment, index) => (
            <li
              key={enrollment.id}
              className="animate-fade-in-up flex items-center justify-between rounded-lg border border-stone-200 bg-white p-4"
              style={{ animationDelay: `${index * 60}ms` }}
            >
              <span className="font-medium text-stone-900">{enrollment.courseTitle}</span>
              <span
                className={`rounded-full px-2.5 py-0.5 text-xs font-semibold ${
                  enrollment.status === "COMPLETED" ? "bg-emerald-100 text-emerald-700" : "bg-indigo-100 text-indigo-700"
                }`}
              >
                {enrollment.status === "COMPLETED" ? "Completed" : "In progress"}
              </span>
            </li>
          ))}
        </ul>
      )}
    </div>
  );
}
