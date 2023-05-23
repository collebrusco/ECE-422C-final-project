package network.controllers;

import network.messages.Message;

public interface IController {
	abstract void execute(Message msg);
}
