import { useState } from "react";
import { CourseList } from "./components/CourseList";
import { CourseDetail } from "./components/CourseDetail";
import { ExtraLessons } from "./components/ExtraLessons";
import { StudyModules } from "./components/StudyModules";
import { Profile } from "./components/Profile";
import { Sidebar, type Section } from "./components/Sidebar";

function App() {
  const [activeSection, setActiveSection] = useState<Section>("courses");
  const [selectedCourseId, setSelectedCourseId] = useState<number | null>(null);

  function navigate(section: Section) {
    setActiveSection(section);
    setSelectedCourseId(null);
  }

  return (
    <div className="flex min-h-screen bg-stone-50">
      <Sidebar active={activeSection} onNavigate={navigate} />

      <main className="mx-auto w-full max-w-4xl px-6 py-10 sm:px-10">
        {activeSection === "courses" &&
          (selectedCourseId === null ? (
            <CourseList onSelect={setSelectedCourseId} />
          ) : (
            <CourseDetail courseId={selectedCourseId} onBack={() => setSelectedCourseId(null)} />
          ))}
        {activeSection === "extra-lessons" && <ExtraLessons />}
        {activeSection === "study-modules" && <StudyModules />}
        {activeSection === "profile" && <Profile onBrowseCourses={() => navigate("courses")} />}
      </main>
    </div>
  );
}

export default App;
