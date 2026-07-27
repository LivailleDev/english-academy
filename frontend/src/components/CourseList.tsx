import { useEffect, useState } from "react";
import { listCourses } from "../api/courses";
import type { Course } from "../api/types";

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

  if (loading) return <p>Loading courses…</p>;
  if (error) return <p className="error">{error}</p>;
  if (courses.length === 0) return <p>No courses yet.</p>;

  return (
    <ul className="course-list">
      {courses.map((course) => (
        <li key={course.id} className="course-card" onClick={() => onSelect(course.id)}>
          <div className="course-card-header">
            <h3>{course.title}</h3>
            <span className="level-badge">{course.level}</span>
          </div>
          <p>{course.description}</p>
          <span className="duration">{course.durationHours}h</span>
        </li>
      ))}
    </ul>
  );
}
