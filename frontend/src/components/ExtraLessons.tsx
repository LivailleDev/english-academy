import { useEffect, useState } from "react";
import { listExtraLessons } from "../api/extraLessons";
import type { ExtraLesson } from "../api/types";
import { CATEGORY_BADGE_STYLES, CATEGORY_LABELS } from "../lib/categoryTheme";

export function ExtraLessons() {
  const [lessons, setLessons] = useState<ExtraLesson[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [expandedId, setExpandedId] = useState<number | null>(null);

  useEffect(() => {
    listExtraLessons()
      .then(setLessons)
      .catch(() => setError("Could not load extra lessons. Is the backend running on :8080?"))
      .finally(() => setLoading(false));
  }, []);

  if (loading) return <p className="text-stone-500">Loading extra lessons…</p>;
  if (error) return <p className="rounded-lg bg-red-50 px-4 py-3 text-sm text-red-700">{error}</p>;

  return (
    <div>
      <h2 className="mb-1 font-serif text-2xl font-semibold text-stone-900">Extra Lessons</h2>
      <p className="mb-6 text-stone-500">Short bonus lessons you can pick up any time, outside a full course.</p>

      {lessons.length === 0 ? (
        <p className="text-sm text-stone-500">No extra lessons yet.</p>
      ) : (
        <ul className="space-y-3">
          {lessons.map((lesson, index) => {
            const expanded = expandedId === lesson.id;
            return (
              <li
                key={lesson.id}
                className="animate-fade-in-up overflow-hidden rounded-xl border border-stone-200 bg-white shadow-sm"
                style={{ animationDelay: `${index * 60}ms` }}
              >
                <button
                  onClick={() => setExpandedId(expanded ? null : lesson.id)}
                  className="flex w-full items-start justify-between gap-3 p-4 text-left transition hover:bg-stone-50"
                >
                  <div>
                    <div className="mb-1 flex items-center gap-2">
                      <span className={`rounded-full px-2.5 py-0.5 text-xs font-semibold ${CATEGORY_BADGE_STYLES[lesson.category]}`}>
                        {CATEGORY_LABELS[lesson.category]}
                      </span>
                      <span className="text-xs text-stone-400">{lesson.durationMinutes} min</span>
                    </div>
                    <h3 className="font-serif text-base font-semibold text-stone-900">{lesson.title}</h3>
                    <p className="mt-0.5 text-sm text-stone-600">{lesson.description}</p>
                  </div>
                  <span className={`shrink-0 text-stone-400 transition-transform ${expanded ? "rotate-180" : ""}`}>▾</span>
                </button>
                {expanded && (
                  <div className="animate-fade-in-up border-t border-stone-100 bg-stone-50 px-4 py-3 text-sm text-stone-700">
                    {lesson.content}
                  </div>
                )}
              </li>
            );
          })}
        </ul>
      )}
    </div>
  );
}
