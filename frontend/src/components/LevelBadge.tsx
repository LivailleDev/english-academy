import type { CourseLevel } from "../api/types";
import { LEVEL_BADGE_STYLES } from "../lib/levelTheme";

export function LevelBadge({ level }: { level: CourseLevel }) {
  return (
    <span className={`inline-flex items-center rounded-full px-2.5 py-0.5 text-xs font-semibold ${LEVEL_BADGE_STYLES[level]}`}>
      {level}
    </span>
  );
}
