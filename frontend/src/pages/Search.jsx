import { useEffect, useState } from "react";
import { searchNotebooks } from "../services/notebookService.js";
import NotebookCard from "../components/NotebookCard.jsx";

export default function Search() {
  const [filters, setFilters] = useState({
    subject: "",
    semester: "",
    branch: "",
    keyword: ""
  });
  const [results, setResults] = useState([]);

  useEffect(() => {
    const fetchData = async () => {
      const data = await searchNotebooks({ ...filters, page: 0, size: 12 });
      setResults(data.content || []);
    };
    fetchData();
  }, []);

  const handleSearch = async (event) => {
    event.preventDefault();
    const data = await searchNotebooks({ ...filters, page: 0, size: 12 });
    setResults(data.content || []);
  };

  return (
    <section className="flex flex-col gap-6">
      <div className="flex flex-col gap-6">
        <form className="grid gap-4 md:grid-cols-5" onSubmit={handleSearch}>
          <input
            className="rounded-xl border border-orange-100 bg-white px-4 py-3"
            placeholder="Subject"
            value={filters.subject}
            onChange={(e) => setFilters({ ...filters, subject: e.target.value })}
          />
          <input
            className="rounded-xl border border-orange-100 bg-white px-4 py-3"
            placeholder="Semester"
            value={filters.semester}
            onChange={(e) => setFilters({ ...filters, semester: e.target.value })}
          />
          <input
            className="rounded-xl border border-orange-100 bg-white px-4 py-3"
            placeholder="Branch"
            value={filters.branch}
            onChange={(e) => setFilters({ ...filters, branch: e.target.value })}
          />
          <input
            className="rounded-xl border border-orange-100 bg-white px-4 py-3"
            placeholder="Keyword"
            value={filters.keyword}
            onChange={(e) => setFilters({ ...filters, keyword: e.target.value })}
          />
          <button className="rounded-full bg-accent px-6 py-3 text-sm font-semibold text-white">
            Search
          </button>
        </form>
        <div className="grid gap-6 md:grid-cols-3">
          {results.map((notebook) => (
            <NotebookCard key={notebook.id} notebook={notebook} />
          ))}
        </div>
      </div>
    </section>
  );
}
