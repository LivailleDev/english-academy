import { useEffect, useState } from "react";
import { getCourse } from "../api/courses";
import { ApiError } from "../api/client";
import { createStudent, enroll, findStudentByEmail } from "../api/enrollments";
import type { Course } from "../api/types";
import { LEVEL_GRADIENTS, LEVEL_LABELS } from "../lib/levelTheme";
import { setCurrentStudentId } from "../lib/currentStudent";

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
      let student;
      try {
        student = await createStudent(name, email);
      } catch (err) {
        if (err instanceof ApiError && err.status === 409) {
          student = await findStudentByEmail(email);
        } else {
          throw err;
        }
      }
      await enroll(student.id, courseId);
      setCurrentStudentId(student.id);
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
    <div className="animate-fade-in-up">
      <button onClick={onBack} className="mb-6 text-sm font-medium text-indigo-600 hover:text-indigo-800">
        ← Back to courses
      </button>

      <div className={`mb-8 rounded-2xl bg-gradient-to-br ${LEVEL_GRADIENTS[course.level]} px-6 py-8 text-white shadow-md sm:px-8`}>
        <span className="text-xs font-semibold tracking-wide text-white/80 uppercase">
          {LEVEL_LABELS[course.level]} · {course.level} · {course.durationHours}h
        </span>
        <h2 className="mt-1 font-serif text-2xl font-semibold sm:text-3xl">{course.title}</h2>
        <p className="mt-2 max-w-2xl text-sm text-white/90 sm:text-base">{course.description}</p>
      </div>

      <div className="grid gap-8 sm:grid-cols-[1.4fr_1fr]">
        <section>
          <h3 className="mb-3 font-serif text-lg font-semibold text-stone-900">Lessons</h3>
          {course.lessons.length === 0 ? (
            <p className="text-sm text-stone-500">No lessons published yet.</p>
          ) : (
            <ol className="space-y-3">
              {course.lessons.map((lesson, index) => (
                <li
                  key={lesson.id}
                  className="animate-fade-in-up rounded-lg border border-stone-200 bg-white p-4 transition hover:border-indigo-300 hover:shadow-sm"
                  style={{ animationDelay: `${index * 70}ms` }}
                >
                  <div className="flex items-baseline gap-2">
                    <span className="flex h-5 w-5 shrink-0 items-center justify-center rounded-full bg-indigo-100 text-[10px] font-bold text-indigo-600">
                      {index + 1}
                    </span>
                    <strong className="text-stone-900">{lesson.title}</strong>
                  </div>
                  <p className="mt-1 pl-7 text-sm text-stone-600">{lesson.content}</p>
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
                className="w-full rounded-md border border-stone-300 px-3 py-2 text-sm text-stone-900 transition focus:border-indigo-400 focus:ring-2 focus:ring-indigo-100 focus:outline-none"
              />
            </div>
            <div>
              <label className="mb-1 block text-xs font-medium text-stone-500">Email</label>
              <input
                type="email"
                value={email}
                onChange={(e) => setEmail(e.target.value)}
                required
                className="w-full rounded-md border border-stone-300 px-3 py-2 text-sm text-stone-900 transition focus:border-indigo-400 focus:ring-2 focus:ring-indigo-100 focus:outline-none"
              />
            </div>
            <button
              type="submit"
              disabled={enrolling}
              className="w-full rounded-md bg-indigo-600 px-3 py-2 text-sm font-semibold text-white transition hover:scale-[1.02] hover:bg-indigo-700 active:scale-[0.98] disabled:scale-100 disabled:opacity-60"
            >
              {enrolling ? "Enrolling…" : "Enroll"}
            </button>
          </form>
          {enrollMessage && (
            <p
              key={enrollMessage.text}
              className={`animate-pop-in mt-3 rounded-lg px-3 py-2 text-sm font-medium ${
                enrollMessage.ok ? "bg-emerald-50 text-emerald-700 ring-1 ring-emerald-200" : "bg-red-50 text-red-700 ring-1 ring-red-200"
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
