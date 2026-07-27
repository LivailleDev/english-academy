import { useEffect, useState } from "react";
import { getCourse } from "../api/courses";
import { ApiError } from "../api/client";
import { createStudent, enroll } from "../api/enrollments";
import type { Course } from "../api/types";

interface Props {
  courseId: number;
  onBack: () => void;
}

export function CourseDetail({ courseId, onBack }: Props) {
  const [course, setCourse] = useState<Course | null>(null);
  const [name, setName] = useState("");
  const [email, setEmail] = useState("");
  const [enrollMessage, setEnrollMessage] = useState<string | null>(null);
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
      setEnrollMessage(`Enrolled! Welcome to ${course?.title}, ${student.name}.`);
      setName("");
      setEmail("");
    } catch (err) {
      if (err instanceof ApiError) {
        setEnrollMessage(err.problem.detail ?? "Could not complete enrollment.");
      } else {
        setEnrollMessage("Could not complete enrollment.");
      }
    } finally {
      setEnrolling(false);
    }
  }

  if (!course) return <p>Loading course…</p>;

  return (
    <div className="course-detail">
      <button className="back-link" onClick={onBack}>
        ← Back to courses
      </button>
      <h2>{course.title}</h2>
      <p className="level-badge">{course.level}</p>
      <p>{course.description}</p>

      <h3>Lessons</h3>
      {course.lessons.length === 0 ? (
        <p>No lessons published yet.</p>
      ) : (
        <ol className="lesson-list">
          {course.lessons.map((lesson) => (
            <li key={lesson.id}>
              <strong>{lesson.title}</strong>
              <p>{lesson.content}</p>
            </li>
          ))}
        </ol>
      )}

      <h3>Enroll</h3>
      <form onSubmit={handleEnroll} className="enroll-form">
        <input
          type="text"
          placeholder="Your name"
          value={name}
          onChange={(e) => setName(e.target.value)}
          required
        />
        <input
          type="email"
          placeholder="Your email"
          value={email}
          onChange={(e) => setEmail(e.target.value)}
          required
        />
        <button type="submit" disabled={enrolling}>
          {enrolling ? "Enrolling…" : "Enroll"}
        </button>
      </form>
      {enrollMessage && <p className="enroll-message">{enrollMessage}</p>}
    </div>
  );
}
