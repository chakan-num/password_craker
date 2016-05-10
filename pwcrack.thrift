namespace java pwcrack

struct Task {
1: string prefix,
}

struct ClientInfo {
1: i32 client_id,
}

service PWCrackService {
	ClientInfo getInfo(), 
	bool isDone(),
	string getPWtoFind(),
	Task getTask(1: i32 c_id),
	void SuccessCrack(1: string ans),
	void ping(1: i32 c_id),
	void deleteNodebyID(1: i32 c_id);
}
