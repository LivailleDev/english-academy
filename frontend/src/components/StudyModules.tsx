import { useEffect, useState } from "react";
import { listStudyModules } from "../api/studyModules";
import type { StudyModule } from "../api/types";
import { LEVEL_GRADIENTS } from "../lib/levelTheme";
import { LevelBadge } from "./LevelBadge";

export function StudyModules() {
  const [modules, setModules] = useState<StudyModule[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    listStudyModules()
      .then(setModules)
      .catch(() => setError("Could not load study modules. Is the backend running on :8080?"))
      .finally(() => setLoading(false));
  }, []);

  if (loading) return <p className="text-stone-500">Loading study modules…</p>;
  if (error) return <p className="rounded-lg bg-red-50 px-4 py-3 text-sm text-red-700">{error}</p>;

  return (
    <div>
      <h2 className="mb-1 font-serif text-2xl font-semibold text-stone-900">Study Modules</h2>
      <p className="mb-6 text-stone-500">Structured learning paths, each covering a sequence of topics.</p>

      {modules.length === 0 ? (
        <p className="text-sm text-stone-500">No study modules yet.</p>
      ) : (
        <ul className="grid gap-4 sm:grid-cols-2">
          {modules.map((module, index) => (
            <li
              key={module.id}
              className="animate-fade-in-up overflow-hidden rounded-xl border border-stone-200 bg-white shadow-sm"
              style={{ animationDelay: `${index * 70}ms` }}
            >
              <span className={`block h-1.5 w-full bg-gradient-to-r ${LEVEL_GRADIENTS[module.level]}`} />
              <div className="p-5">
                <div className="mb-1.5 flex items-center justify-between gap-2">
                  <h3 className="font-serif text-lg font-semibold text-stone-900">{module.title}</h3>
                  <LevelBadge level={module.level} />
                </div>
                <p className="mb-3 text-sm text-stone-600">{module.description}</p>
                <ol className="space-y-1.5">
                  {module.topics.map((topic, i) => (
                    <li key={i} className="flex items-center gap-2 text-sm text-stone-700">
                      <span className="flex h-4 w-4 shrink-0 items-center justify-center rounded-full bg-emerald-100 text-[9px] font-bold text-emerald-700">
                        {i + 1}
                      </span>
                      {topic}
                    </li>
                  ))}
                </ol>
              </div>
            </li>
          ))}
        </ul>
      )}
    </div>
  );
}
