import { MascotHorse } from "./MascotHorse";

export type Section = "courses" | "extra-lessons" | "study-modules" | "profile";

interface NavItem {
  section: Section;
  label: string;
  dotColor: string;
}

const NAV_ITEMS: NavItem[] = [
  { section: "courses", label: "Courses", dotColor: "bg-indigo-500" },
  { section: "extra-lessons", label: "Extra Lessons", dotColor: "bg-amber-500" },
  { section: "study-modules", label: "Study Modules", dotColor: "bg-emerald-500" },
  { section: "profile", label: "Profile", dotColor: "bg-rose-500" },
];

interface Props {
  active: Section;
  onNavigate: (section: Section) => void;
}

export function Sidebar({ active, onNavigate }: Props) {
  return (
    <aside className="flex w-56 shrink-0 flex-col border-r border-stone-200 bg-white">
      <div className="flex items-center gap-2 border-b border-stone-200 px-5 py-6">
        <MascotHorse className="h-9 w-9 shrink-0" />
        <span className="font-serif text-lg font-semibold text-stone-900">English Academy</span>
      </div>

      <nav className="flex flex-col gap-1 p-3">
        {NAV_ITEMS.map((item) => {
          const isActive = item.section === active;
          return (
            <button
              key={item.section}
              onClick={() => onNavigate(item.section)}
              className={`flex items-center gap-2.5 rounded-lg px-3 py-2.5 text-left text-sm font-medium transition ${
                isActive ? "bg-indigo-50 text-indigo-700" : "text-stone-600 hover:bg-stone-100"
              }`}
            >
              <span className={`h-2 w-2 shrink-0 rounded-full ${item.dotColor}`} />
              {item.label}
            </button>
          );
        })}
      </nav>
    </aside>
  );
}
