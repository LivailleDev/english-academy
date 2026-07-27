import { useState } from "react";
import { CourseList } from "./components/CourseList";
import { CourseDetail } from "./components/CourseDetail";

function App() {
  const [selectedCourseId, setSelectedCourseId] = useState<number | null>(null);

  return (
    <div className="min-h-screen bg-stone-50">
      <header className="border-b border-stone-200 bg-white">
        <div className="mx-auto max-w-4xl px-6 py-8">
          <h1 className="font-serif text-3xl font-semibold text-stone-900">English Academy</h1>
          <p className="mt-1 text-stone-500">Browse courses, check the lessons, and enroll.</p>
        </div>
      </header>
      <main className="mx-auto max-w-4xl px-6 py-10">
        {selectedCourseId === null ? (
          <CourseList onSelect={setSelectedCourseId} />
        ) : (
          <CourseDetail courseId={selectedCourseId} onBack={() => setSelectedCourseId(null)} />
        )}
      </main>
    </div>
  );
}

export default App;
