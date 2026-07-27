import { useState } from "react";
import { CourseList } from "./components/CourseList";
import { CourseDetail } from "./components/CourseDetail";
import "./App.css";

function App() {
  const [selectedCourseId, setSelectedCourseId] = useState<number | null>(null);

  return (
    <div className="app">
      <header>
        <h1>English Academy</h1>
        <p>Browse courses, check the lessons, and enroll.</p>
      </header>
      <main>
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
