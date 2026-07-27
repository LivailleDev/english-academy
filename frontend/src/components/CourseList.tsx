import { useEffect, useState } from "react";
import { listCourses } from "../api/courses";
import type { Course } from "../api/types";
import { LevelBadge } from "./LevelBadge";

interface Props {
  onSelect: (courseId: number) => void;
}

export function CourseList({ onSelect }: Props) {
  const [courses, setCourses] = useState<Course[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    listCourses()
      .then(setCourses)
      .catch(() => setError("Could not load courses. Is the backend running on :8080?"))
      .finally(() => setLoading(false));
  }, []);

  if (loading) {
    return <p className="text-stone-500">Loading courses…</p>;
  }

  if (error) {
    return <p className="rounded-lg bg-red-50 px-4 py-3 text-sm text-red-700">{error}</p>;
  }

  if (courses.length === 0) {
    return <p className="text-stone-500">No courses yet.</p>;
  }

  return (
    <ul className="grid gap-4 sm:grid-cols-2">
      {courses.map((course) => (
        <li key={course.id}>
          <button
            onClick={() => onSelect(course.id)}
            className="flex h-full w-full flex-col gap-3 rounded-xl border border-stone-200 bg-white p-5 text-left shadow-sm transition hover:-translate-y-0.5 hover:border-indigo-300 hover:shadow-md"
          >
            <div className="flex items-start justify-between gap-2">
              <h3 className="font-serif text-lg font-semibold text-stone-900">{course.title}</h3>
              <LevelBadge level={course.level} />
            </div>
            <p className="line-clamp-3 flex-1 text-sm text-stone-600">{course.description}</p>
            <span className="text-xs font-medium text-stone-400">{course.durationHours}h course</span>
          </button>
        </li>
      ))}
    </ul>
  );
}
