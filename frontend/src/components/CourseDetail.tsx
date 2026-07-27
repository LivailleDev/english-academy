import { useEffect, useState } from "react";
import { getCourse } from "../api/courses";
import { ApiError } from "../api/client";
import { createStudent, enroll } from "../api/enrollments";
import type { Course } from "../api/types";
import { LevelBadge } from "./LevelBadge";

interface Props {
  courseId: number;
  onBack: () => void;
}

export function CourseDetail({ courseId, onBack }: Props) {
  const [course, setCourse] = useState<Course | null>(null);
  const [name, setName] = useState("");
  const [email, setEmail] = useState("");
  const [enrollMessage, setEnrollMessage] = useState<{ text: string; ok: boolean } | null>(null);
  const [enrolling, setEnrolling] = useState(false);

  useEffect(() => {
    getCourse(courseId).then(setCourse);
  }, [courseId]);

  async function handleEnroll(event: React.FormEvent) {
    event.preventDefault();
    setEnrolling(true);
    setEnrollMessage(null);
    try {
      const student = await createStudent(name, email);
      await enroll(student.id, courseId);
      setEnrollMessage({ text: `Enrolled! Welcome to ${course?.title}, ${student.name}.`, ok: true });
      setName("");
      setEmail("");
    } catch (err) {
      const detail = err instanceof ApiError ? err.problem.detail : undefined;
      setEnrollMessage({ text: detail ?? "Could not complete enrollment.", ok: false });
    } finally {
      setEnrolling(false);
    }
  }

  if (!course) {
    return <p className="text-stone-500">Loading course…</p>;
  }

  return (
    <div>
      <button onClick={onBack} className="mb-6 text-sm font-medium text-indigo-600 hover:text-indigo-800">
        ← Back to courses
      </button>

      <div className="mb-2 flex items-center gap-3">
        <h2 className="font-serif text-2xl font-semibold text-stone-900">{course.title}</h2>
        <LevelBadge level={course.level} />
      </div>
      <p className="mb-8 text-stone-600">{course.description}</p>

      <div className="grid gap-8 sm:grid-cols-[1.4fr_1fr]">
        <section>
          <h3 className="mb-3 font-serif text-lg font-semibold text-stone-900">Lessons</h3>
          {course.lessons.length === 0 ? (
            <p className="text-sm text-stone-500">No lessons published yet.</p>
          ) : (
            <ol className="space-y-3">
              {course.lessons.map((lesson, index) => (
                <li key={lesson.id} className="rounded-lg border border-stone-200 bg-white p-4">
                  <div className="flex items-baseline gap-2">
                    <span className="text-xs font-semibold text-indigo-500">{index + 1}</span>
                    <strong className="text-stone-900">{lesson.title}</strong>
                  </div>
                  <p className="mt-1 text-sm text-stone-600">{lesson.content}</p>
                </li>
              ))}
            </ol>
          )}
        </section>

        <section>
          <h3 className="mb-3 font-serif text-lg font-semibold text-stone-900">Enroll</h3>
          <form onSubmit={handleEnroll} className="space-y-3 rounded-lg border border-stone-200 bg-white p-4">
            <div>
              <label className="mb-1 block text-xs font-medium text-stone-500">Name</label>
              <input
                type="text"
                value={name}
                onChange={(e) => setName(e.target.value)}
                required
                className="w-full rounded-md border border-stone-300 px-3 py-2 text-sm text-stone-900 focus:border-indigo-400 focus:ring-1 focus:ring-indigo-400 focus:outline-none"
              />
            </div>
            <div>
              <label className="mb-1 block text-xs font-medium text-stone-500">Email</label>
              <input
                type="email"
                value={email}
                onChange={(e) => setEmail(e.target.value)}
                required
                className="w-full rounded-md border border-stone-300 px-3 py-2 text-sm text-stone-900 focus:border-indigo-400 focus:ring-1 focus:ring-indigo-400 focus:outline-none"
              />
            </div>
            <button
              type="submit"
              disabled={enrolling}
              className="w-full rounded-md bg-indigo-600 px-3 py-2 text-sm font-semibold text-white transition hover:bg-indigo-700 disabled:opacity-60"
            >
              {enrolling ? "Enrolling…" : "Enroll"}
            </button>
          </form>
          {enrollMessage && (
            <p
              className={`mt-3 rounded-lg px-3 py-2 text-sm ${
                enrollMessage.ok ? "bg-emerald-50 text-emerald-700" : "bg-red-50 text-red-700"
              }`}
            >
              {enrollMessage.text}
            </p>
          )}
        </section>
      </div>
    </div>
  );
}
