import { useEffect, useState, useRef } from "react";
import { Send } from "lucide-react";
import toast from "react-hot-toast";
import { Button } from "@/components/ui/button";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Input } from "@/components/ui/input";
import api from "@/api/axios";
import useAuthStore from "@/store/authStore";

export default function ChatPage() {
  const [rooms, setRooms] = useState([]);
  const [messages, setMessages] = useState([]);
  const [activeRoom, setActiveRoom] = useState(null);
  const [newMessage, setNewMessage] = useState("");
  const [projectId] = useState("1");
  const user = useAuthStore((s) => s.user);
  const messagesEnd = useRef(null);

  useEffect(() => {
    api.get(`/api/projects/${projectId}/chat/rooms`)
      .then((res) => {
        const roomList = res.data.data?.content || [];
        setRooms(roomList);
        if (roomList.length > 0) setActiveRoom(roomList[0]);
      })
      .catch(() => {});
  }, [projectId]);

  useEffect(() => {
    if (!activeRoom) return;
    api.get(`/api/projects/${projectId}/chat/rooms/${activeRoom.id}/messages`)
      .then((res) => setMessages((res.data.data?.content || []).reverse()))
      .catch(() => {});
  }, [activeRoom, projectId]);

  useEffect(() => {
    messagesEnd.current?.scrollIntoView({ behavior: "smooth" });
  }, [messages]);

  const sendMessage = async () => {
    if (!newMessage.trim() || !activeRoom) return;
    try {
      await api.post(`/api/projects/${projectId}/chat/rooms/${activeRoom.id}/messages`, { content: newMessage });
      setNewMessage("");
      const res = await api.get(`/api/projects/${projectId}/chat/rooms/${activeRoom.id}/messages`);
      setMessages((res.data.data?.content || []).reverse());
    } catch {
      toast.error("Failed to send message");
    }
  };

  const createRoom = async () => {
    try {
      const res = await api.post(`/api/projects/${projectId}/chat/rooms`, { name: "General", isGroup: true });
      setRooms([...rooms, res.data.data]);
      setActiveRoom(res.data.data);
    } catch {
      toast.error("Failed to create room");
    }
  };

  return (
    <div>
      <div className="mb-6 flex items-center justify-between">
        <div>
          <h1 className="text-3xl font-bold">Team Chat</h1>
          <p className="text-black">Collaborate with your team in real-time</p>
        </div>
        <Button variant="outline" onClick={createRoom}>New Room</Button>
      </div>

      <div className="grid grid-cols-1 lg:grid-cols-4 gap-6 h-[calc(100vh-200px)]">
        <Card className="border-0 shadow-sm">
          <CardHeader><CardTitle className="text-sm">Rooms</CardTitle></CardHeader>
          <CardContent className="space-y-1">
            {rooms.map((room) => (
              <button
                key={room.id}
                onClick={() => setActiveRoom(room)}
                className={`w-full text-left px-3 py-2 rounded-lg text-sm ${
                  activeRoom?.id === room.id ? "bg-slate-900 text-white" : "hover:bg-slate-50"
                }`}
              >
                {room.name}
              </button>
            ))}
            {rooms.length === 0 && <p className="text-sm text-slate-400">No rooms yet</p>}
          </CardContent>
        </Card>

        <Card className="border-0 shadow-sm lg:col-span-3 flex flex-col">
          <CardHeader><CardTitle className="text-sm">{activeRoom?.name || "Select a room"}</CardTitle></CardHeader>
          <CardContent className="flex-1 flex flex-col">
            <div className="flex-1 overflow-y-auto space-y-3 mb-4">
              {messages.map((msg) => (
                <div key={msg.id} className={`flex ${msg.senderId === user?.id ? "justify-end" : "justify-start"}`}>
                  <div className={`max-w-[70%] px-4 py-2 rounded-2xl text-sm ${
                    msg.senderId === user?.id ? "bg-slate-900 text-white" : "bg-slate-100"
                  }`}>
                    {msg.senderId !== user?.id && <p className="text-xs font-medium mb-1 opacity-70">{msg.senderName}</p>}
                    {msg.content}
                  </div>
                </div>
              ))}
              <div ref={messagesEnd} />
            </div>
            <div className="flex gap-2">
              <Input
                placeholder="Type a message..."
                value={newMessage}
                onChange={(e) => setNewMessage(e.target.value)}
                onKeyDown={(e) => e.key === "Enter" && sendMessage()}
              />
              <Button onClick={sendMessage}><Send size={16} /></Button>
            </div>
          </CardContent>
        </Card>
      </div>
    </div>
  );
}
