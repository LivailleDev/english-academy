export type CourseLevel = "A1" | "A2" | "B1" | "B2" | "C1" | "C2";

export interface Lesson {
  id: number;
  title: string;
  content: string;
  orderIndex: number;
}

export interface Course {
  id: number;
  title: string;
  description: string;
  level: CourseLevel;
  durationHours: number;
  createdAt: string;
  lessons: Lesson[];
}

export interface Student {
  id: number;
  name: string;
  email: string;
  createdAt: string;
}

export type EnrollmentStatus = "IN_PROGRESS" | "COMPLETED";

export interface Enrollment {
  id: number;
  studentId: number;
  courseId: number;
  courseTitle: string;
  status: EnrollmentStatus;
  enrolledAt: string;
}

export interface ProblemDetail {
  title: string;
  detail: string;
  status: number;
}

export type LessonCategory = "GRAMMAR" | "VOCABULARY" | "PRONUNCIATION" | "IDIOMS" | "CULTURE";

export interface ExtraLesson {
  id: number;
  title: string;
  description: string;
  content: string;
  category: LessonCategory;
  durationMinutes: number;
}

export interface StudyModule {
  id: number;
  title: string;
  description: string;
  level: CourseLevel;
  topics: string[];
}
