import { useEffect, useState } from "react";
import type { Course } from "../api/types";
import { LEVEL_GRADIENTS, LEVEL_LABELS } from "../lib/levelTheme";

interface Props {
  courses: Course[];
  onSelect: (courseId: number) => void;
}

const AUTO_ADVANCE_MS = 4500;

export function CourseCarousel({ courses, onSelect }: Props) {
  const [index, setIndex] = useState(0);
  const [paused, setPaused] = useState(false);

  useEffect(() => {
    if (paused || courses.length <= 1) return;
    const timer = setInterval(() => {
      setIndex((i) => (i + 1) % courses.length);
    }, AUTO_ADVANCE_MS);
    return () => clearInterval(timer);
  }, [paused, courses.length]);

  if (courses.length === 0) return null;

  const goTo = (i: number) => setIndex((i + courses.length) % courses.length);

  return (
    <div
      className="relative mb-10 overflow-hidden rounded-2xl shadow-lg"
      onMouseEnter={() => setPaused(true)}
      onMouseLeave={() => setPaused(false)}
    >
      <div
        className="flex transition-transform duration-700 ease-out"
        style={{ transform: `translateX(-${index * 100}%)` }}
      >
        {courses.map((course) => (
          <button
            key={course.id}
            onClick={() => onSelect(course.id)}
            className={`flex w-full shrink-0 flex-col justify-end gap-2 bg-gradient-to-br ${LEVEL_GRADIENTS[course.level]} px-8 py-12 text-left text-white sm:px-12 sm:py-16`}
          >
            <span className="text-xs font-semibold tracking-wide text-white/80 uppercase">
              {LEVEL_LABELS[course.level]} · {course.level}
            </span>
            <h2 className="font-serif text-2xl font-semibold sm:text-3xl">{course.title}</h2>
            <p className="max-w-xl text-sm text-white/90 sm:text-base">{course.description}</p>
            <span className="mt-3 inline-flex w-fit items-center gap-1 rounded-full bg-white/15 px-4 py-1.5 text-sm font-medium backdrop-blur-sm transition group-hover:bg-white/25">
              Explore this course →
            </span>
          </button>
        ))}
      </div>

      {courses.length > 1 && (
        <>
          <button
            aria-label="Previous course"
            onClick={() => goTo(index - 1)}
            className="absolute top-1/2 left-3 -translate-y-1/2 rounded-full bg-white/20 p-2 text-white backdrop-blur-sm transition hover:bg-white/35"
          >
            ←
          </button>
          <button
            aria-label="Next course"
            onClick={() => goTo(index + 1)}
            className="absolute top-1/2 right-3 -translate-y-1/2 rounded-full bg-white/20 p-2 text-white backdrop-blur-sm transition hover:bg-white/35"
          >
            →
          </button>
          <div className="absolute bottom-4 left-1/2 flex -translate-x-1/2 gap-2">
            {courses.map((course, i) => (
              <button
                key={course.id}
                aria-label={`Go to slide ${i + 1}`}
                onClick={() => goTo(i)}
                className={`h-2 rounded-full transition-all ${i === index ? "w-6 bg-white" : "w-2 bg-white/50 hover:bg-white/75"}`}
              />
            ))}
          </div>
        </>
      )}
    </div>
  );
}
