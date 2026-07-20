import { useEffect, useState } from "react";
import { User, Lock } from "lucide-react";
import toast from "react-hot-toast";
import { Button } from "@/components/ui/button";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import api from "@/api/axios";
import ENDPOINTS from "@/api/endpoints";
import useAuthStore from "@/store/authStore";

export default function ProfilePage() {
  const { user, setAuth } = useAuthStore();
  const [profile, setProfile] = useState({ firstName: "", lastName: "", avatar: "" });
  const [passwords, setPasswords] = useState({ currentPassword: "", newPassword: "" });

  useEffect(() => {
    api.get(ENDPOINTS.PROFILE.BASE)
      .then((res) => setProfile(res.data.data))
      .catch(() => {
        if (user) setProfile({ firstName: user.firstName, lastName: user.lastName, avatar: user.avatar || "" });
      });
  }, [user]);

  const updateProfile = async () => {
    try {
      const res = await api.put(ENDPOINTS.PROFILE.BASE, profile);
      setAuth({ user: res.data.data, accessToken: useAuthStore.getState().accessToken, refreshToken: useAuthStore.getState().refreshToken });
      toast.success("Profile updated");
    } catch {
      toast.error("Update failed");
    }
  };

  const changePassword = async () => {
    try {
      await api.put(ENDPOINTS.PROFILE.PASSWORD, passwords);
      toast.success("Password changed");
      setPasswords({ currentPassword: "", newPassword: "" });
    } catch (err) {
      toast.error(err.response?.data?.message || "Password change failed");
    }
  };

  return (
    <div>
      <div className="mb-8">
        <h1 className="text-3xl font-bold">Profile</h1>
        <p className="text-black">Manage your account settings</p>
      </div>

      <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
        <Card className="border-0 shadow-sm">
          <CardHeader>
            <CardTitle className="flex items-center gap-2"><User size={18} /> Personal Info</CardTitle>
          </CardHeader>
          <CardContent className="space-y-4">
            <div className="flex items-center gap-4 mb-4">
              <div className="h-16 w-16 rounded-full bg-slate-900 text-white flex items-center justify-center text-xl font-bold">
                {profile.firstName?.[0]}{profile.lastName?.[0]}
              </div>
              <div>
                <p className="font-medium">{user?.email}</p>
                <p className="text-sm text-slate-400">{user?.roles?.join(", ")}</p>
              </div>
            </div>
            <div>
              <Label>First Name</Label>
              <Input value={profile.firstName || ""} onChange={(e) => setProfile({ ...profile, firstName: e.target.value })} />
            </div>
            <div>
              <Label>Last Name</Label>
              <Input value={profile.lastName || ""} onChange={(e) => setProfile({ ...profile, lastName: e.target.value })} />
            </div>
            <Button onClick={updateProfile}>Save Changes</Button>
          </CardContent>
        </Card>

        <Card className="border-0 shadow-sm">
          <CardHeader>
            <CardTitle className="flex items-center gap-2"><Lock size={18} /> Change Password</CardTitle>
          </CardHeader>
          <CardContent className="space-y-4">
            <div>
              <Label>Current Password</Label>
              <Input type="password" value={passwords.currentPassword} onChange={(e) => setPasswords({ ...passwords, currentPassword: e.target.value })} />
            </div>
            <div>
              <Label>New Password</Label>
              <Input type="password" value={passwords.newPassword} onChange={(e) => setPasswords({ ...passwords, newPassword: e.target.value })} />
            </div>
            <Button onClick={changePassword}>Update Password</Button>
          </CardContent>
        </Card>
      </div>
    </div>
  );
}
