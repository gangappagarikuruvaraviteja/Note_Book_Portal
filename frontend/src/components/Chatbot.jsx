import { useState } from "react";

export default function Chatbot({ open, onClose }) {
  const [messages, setMessages] = useState([]);
  const [input, setInput] = useState("");
  const [loading, setLoading] = useState(false);

  const send = async () => {
    if (!input.trim()) return;
    const userMsg = { role: "user", text: input };
    setMessages((m) => [...m, userMsg]);
    setInput("");
    setLoading(true);
    try {
      const res = await fetch("/api/ai/chat", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ messages: [...messages, userMsg] }),
      });
      const data = await res.json();
      const bot = { role: "assistant", text: data.reply || data.summary || "(no response)" };
      setMessages((m) => [...m, bot]);
    } catch (e) {
      setMessages((m) => [...m, { role: "assistant", text: "Error contacting AI service." }]);
    } finally {
      setLoading(false);
    }
  };

  if (!open) return null;

  return (
    <aside className="fixed right-0 top-0 h-full w-80 bg-white/95 border-l border-white/70 shadow-lg z-40 flex flex-col">
      <div className="px-4 py-3 flex items-center justify-between border-b">
        <h3 className="font-semibold">AI Chat</h3>
        <button className="text-sm text-slate/70" onClick={onClose}>Close</button>
      </div>
      <div className="flex-1 overflow-auto p-4 space-y-3">
        {messages.length === 0 && <div className="text-sm text-slate/60">Ask questions about notebooks or request summaries.</div>}
        {messages.map((m, i) => (
          <div key={i} className={m.role === 'user' ? 'text-right' : 'text-left'}>
            <div className={`inline-block rounded-md px-3 py-2 ${m.role === 'user' ? 'bg-accent text-white' : 'bg-slate/10 text-slate'}`}>
              {m.text}
            </div>
          </div>
        ))}
      </div>
      <div className="p-3 border-t">
        <div className="flex gap-2">
          <input value={input} onChange={(e) => setInput(e.target.value)} placeholder="Ask the AI..." className="flex-1 rounded-md border px-3 py-2" />
          <button disabled={loading} onClick={send} className="bg-accent text-white px-3 rounded-md">{loading ? '...' : 'Send'}</button>
        </div>
      </div>
    </aside>
  );
}
