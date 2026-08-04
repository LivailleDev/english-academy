import capuccinoImage from "../assets/capuccino.jpg";

interface Props {
  className?: string;
}

/** Capuccino — the site's horse mascot. */
export function MascotHorse({ className = "h-16 w-16" }: Props) {
  return (
    <img
      src={capuccinoImage}
      alt="Capuccino, the course mascot"
      className={`rounded-full object-cover object-[center_30%] ring-2 ring-white shadow ${className}`}
    />
  );
}
