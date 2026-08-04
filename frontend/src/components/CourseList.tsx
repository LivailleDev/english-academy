import { useEffect, useMemo, useState } from "react";
import { listCourses } from "../api/courses";
import type { Course } from "../api/types";
import { LevelBadge } from "./LevelBadge";
import { CourseCarousel } from "./CourseCarousel";
import { MascotHorse } from "./MascotHorse";
import { LEVEL_GRADIENTS, LEVEL_LABELS, TIERS, TIER_PILL_STYLES, type Tier } from "../lib/levelTheme";

interface Props {
  onSelect: (courseId: number) => void;
}

export function CourseList({ onSelect }: Props) {
  const [courses, setCourses] = useState<Course[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [tierFilter, setTierFilter] = useState<Tier | "All">("All");

  useEffect(() => {
    listCourses()
      .then(setCourses)
      .catch(() => setError("Could not load courses. Is the backend running on :8080?"))
      .finally(() => setLoading(false));
  }, []);

  const filteredCourses = useMemo(
    () => (tierFilter === "All" ? courses : courses.filter((c) => LEVEL_LABELS[c.level] === tierFilter)),
    [courses, tierFilter]
  );

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
    <div>
      <div className="mb-8 flex items-center gap-4">
        <MascotHorse className="h-20 w-20 shrink-0 animate-float" />
        <div className="relative rounded-2xl rounded-bl-none bg-white px-5 py-3 shadow-sm ring-1 ring-stone-200">
          <p className="text-sm text-stone-700">
            Hi, I'm <span className="font-semibold text-indigo-600">Capuccino</span>! Pick a course below and
            let's get started.
          </p>
        </div>
      </div>

      <CourseCarousel courses={courses} onSelect={onSelect} />

      <div className="mb-4 flex flex-wrap items-center justify-between gap-3">
        <h2 className="font-serif text-lg font-semibold text-stone-900">All courses</h2>
        <div className="flex flex-wrap gap-2">
          <button
            onClick={() => setTierFilter("All")}
            className={`rounded-full px-3.5 py-1.5 text-xs font-semibold transition ${
              tierFilter === "All" ? "bg-stone-800 text-white" : "bg-white text-stone-600 ring-1 ring-stone-200 hover:bg-stone-100"
            }`}
          >
            All
          </button>
          {TIERS.map((tier) => (
            <button
              key={tier}
              onClick={() => setTierFilter(tier)}
              className={`rounded-full px-3.5 py-1.5 text-xs font-semibold transition ${
                tierFilter === tier
                  ? TIER_PILL_STYLES[tier]
                  : "bg-white text-stone-600 ring-1 ring-stone-200 hover:bg-stone-100"
              }`}
            >
              {tier}
            </button>
          ))}
        </div>
      </div>

      {filteredCourses.length === 0 ? (
        <p className="text-sm text-stone-500">No courses at this level yet.</p>
      ) : (
        <ul className="grid gap-4 sm:grid-cols-2">
          {filteredCourses.map((course, index) => (
            <li key={course.id} className="animate-fade-in-up" style={{ animationDelay: `${index * 60}ms` }}>
              <button
                onClick={() => onSelect(course.id)}
                className="group flex h-full w-full flex-col overflow-hidden rounded-xl border border-stone-200 bg-white text-left shadow-sm transition hover:-translate-y-1 hover:shadow-lg"
              >
                <span className={`h-1.5 w-full bg-gradient-to-r ${LEVEL_GRADIENTS[course.level]}`} />
                <div className="flex flex-1 flex-col gap-3 p-5">
                  <div className="flex items-start justify-between gap-2">
                    <h3 className="font-serif text-lg font-semibold text-stone-900 transition group-hover:text-indigo-600">
                      {course.title}
                    </h3>
                    <LevelBadge level={course.level} />
                  </div>
                  <p className="line-clamp-3 flex-1 text-sm text-stone-600">{course.description}</p>
                  <span className="text-xs font-medium text-stone-400">{course.durationHours}h course</span>
                </div>
              </button>
            </li>
          ))}
        </ul>
      )}
    </div>
  );
}
